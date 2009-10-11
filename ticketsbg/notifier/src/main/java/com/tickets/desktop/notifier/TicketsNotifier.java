package com.tickets.desktop.notifier;

import java.awt.AWTException;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemColor;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.Protocol;

public class TicketsNotifier {

    private static final int HTTPS_PORT = 443;
    private static final int HTTP_PORT = 8080;
    private static final String HOST_NAME = "localhost";
    protected static final String SERVLET_PATH = "/tickets/updates";
    private static final boolean USE_SSL = false;

    protected static final String FIRM_KEY_SETTING = "firm.key";
    protected static final String FROM_STOP_SETTING = "from.stop";
    protected static final String LAST_CHECK_SETTING = "last.check";

    protected static final String CONFIG_FILE_NAME = "ticketsNotifier.config";
    private static final long POLL_INTERVAL = 3 * 60 * 1000;

    private HttpClient httpClient;

    private Properties settings = new Properties();

    protected String firmKey;
    protected String fromStop;
    protected long lastCheck = 0;

    private Timer poller;

    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new TicketsNotifier();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }

    public TicketsNotifier() throws IOException {
        try {
            loadSettings();
        } catch (Exception ex) {
            // ignore the case when there is no config file
        }

        poller = new Timer();

        poller.schedule(new PollerTask(getHttpClient()), 0, POLL_INTERVAL);

        final PopupMenu popUp = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage("images/icon.png"));
        trayIcon.setImageAutoSize(true);

        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem settingsItem = new MenuItem(Messages.getString("settings"));
        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsPanel sp = new SettingsPanel(firmKey, fromStop);
                // If settings have been saved, reload them
                if (sp.open()) {
                    loadSettings();
                }
            }
        });

        MenuItem exitItem = new MenuItem(Messages.getString("exit"));
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        // Add components to popUp menu
        popUp.add(settingsItem);
        popUp.addSeparator();
        popUp.add(exitItem);

        trayIcon.setPopupMenu(popUp);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    private Image createImage(String string) throws IOException {
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(
                string);
        return ImageIO.read(is);
    }

    protected static String getConfigFileName() {
        String configFileName = System.getProperty("user.home")
                + System.getProperty("file.separator") + CONFIG_FILE_NAME;
        return configFileName;
    }

    /**
     * Loads the dialog settings from the dialog configuration file. These
     * settings consist of a single value - the last used library file name with
     * its full path.
     */
    private void loadSettings() {
        try {
            String configFileName = getConfigFileName();
            FileInputStream configFileStream = new FileInputStream(
                    configFileName);
            try {
                settings = new Properties();
                settings.load(configFileStream);

                firmKey = settings.getProperty(FIRM_KEY_SETTING);
                fromStop = settings.getProperty(FROM_STOP_SETTING);
                String lastCheckString = settings
                        .getProperty(LAST_CHECK_SETTING);
                if (lastCheckString != null) {
                    lastCheck = Long.parseLong(lastCheckString);
                }
            } finally {
                configFileStream.close();
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private HttpClient getHttpClient() {
        if (httpClient == null) {
            Protocol myhttps = new Protocol("https",
                    new EasySSLProtocolSocketFactory(), HTTPS_PORT);

            httpClient = new HttpClient();

            if (USE_SSL) {
                httpClient.getHostConfiguration().setHost(HOST_NAME,
                        HTTPS_PORT, myhttps);
            } else {
                httpClient.getHostConfiguration().setHost(HOST_NAME, HTTP_PORT);
            }

            httpClient.getParams().setParameter(
                    "http.protocol.content-charset", "UTF-8");

            // Below is the proxy identification
            try {
                String proxyHost = System.getProperty("https.proxyHost");
                int proxyPort = 0;
                try {
                    proxyPort = Integer.parseInt(System
                            .getProperty("https.proxyPort"));
                } catch (Exception ex) {
                    //
                }

                System.setProperty("java.net.useSystemProxies", "true");

                ProxySelector ps = ProxySelector.getDefault();
                List<Proxy> proxyList = ps.select(new URI("https://"
                        + HOST_NAME + SERVLET_PATH));
                Proxy proxy = proxyList.get(0);
                if (proxy != null) {
                    InetSocketAddress addr = ((InetSocketAddress) proxy
                            .address());
                    if (addr != null) {
                        proxyHost = addr.getHostName();
                        proxyPort = addr.getPort();
                    }
                }

                boolean useProxy = proxyHost != null && proxyHost.length() > 0;

                if (useProxy) {
                    httpClient.getHostConfiguration().setProxy(proxyHost,
                            proxyPort);
                }

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

        return httpClient;
    }

    class PollerTask extends TimerTask {

        private HttpClient client;

        public PollerTask(HttpClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            if (firmKey == null || fromStop == null) {
                return;
            }

            PostMethod method = new PostMethod(SERVLET_PATH);
            try {
                method.addParameter("firmKey", firmKey);
                method.addParameter("fromStop", fromStop);
                method.addParameter("lastCheck", "" + lastCheck);
                client.executeMethod(method);

                String body = method.getResponseBodyAsString();
                String[] response = body.split(":");
                int newTickets = Integer.parseInt(response[0]);
                lastCheck = Long.parseLong(response[1]);
                saveLastCheck();

                if (newTickets > 0) {
                    if (newTickets == 1) {
                        JOptionPane.showMessageDialog(null, Messages
                                .getString("newTicket"));
                    } else {
                        JOptionPane.showMessageDialog(null, Messages.getString(
                                "newTickets", newTickets));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                method.releaseConnection();
            }

        }

        public HttpClient getClient() {
            return client;
        }

        public void setClient(HttpClient client) {
            this.client = client;
        }
    }

    private void saveLastCheck() {
        // Create a list of settings to store in the config file

        settings
                .setProperty(TicketsNotifier.LAST_CHECK_SETTING, "" + lastCheck);

        // Save the settings in the config file
        String configFileName = TicketsNotifier.getConfigFileName();
        try {
            FileOutputStream configFileStream = new FileOutputStream(
                    configFileName);
            try {
                settings.store(configFileStream, "");
            } finally {
                configFileStream.close();
            }
        } catch (IOException ex) {
            // ingore
        }
    }

}

class SettingsPanel extends JDialog {
    private JTextField firmKeyField;
    private JTextField fromStopField;

    private boolean saved = false;

    public SettingsPanel(String firmKey, String fromStop) {
        setResizable(false);
        setTitle(Messages.getString("settings"));
        firmKeyField = new JTextField(15);
        fromStopField = new JTextField(15);

        firmKeyField.setText(firmKey);
        fromStopField.setText(fromStop);

        this.setBackground(SystemColor.control);

        getContentPane().setLayout(new GridLayout(3, 2));

        getContentPane().add(new JLabel(Messages.getString("firmKey") + ": "));
        getContentPane().add(firmKeyField);

        getContentPane().add(new JLabel(Messages.getString("fromStop") + ": "));
        getContentPane().add(fromStopField);

        getContentPane().add(new JLabel());
        JButton saveButton = new JButton(Messages.getString("save"));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveSettings();
                    saved = true;
                    setVisible(false);
                } catch (IOException ex) {
                    // ingore
                }
            }
        });
        getContentPane().add(saveButton);

        getRootPane().setDefaultButton(saveButton);
        pack();
    }

    public boolean open() {
        setLocationRelativeTo(null);
        setModal(true);
        // blocking
        setVisible(true);

        return saved;
    }

    /**
     * Saves the dialog settings to the dialog configuration file. These
     * settings consist of a single value - the last used library file name with
     * its full path.
     */
    private void saveSettings() throws IOException {
        // Create a list of settings to store in the config file
        Properties configProps = new Properties();
        String firmKey = firmKeyField.getText();
        String fromStop = fromStopField.getText();

        configProps.setProperty(TicketsNotifier.FIRM_KEY_SETTING, firmKey);
        configProps.setProperty(TicketsNotifier.FROM_STOP_SETTING, fromStop);

        // Save the settings in the config file
        String configFileName = TicketsNotifier.getConfigFileName();
        FileOutputStream configFileStream = new FileOutputStream(configFileName);
        try {
            configProps.store(configFileStream, "");
        } finally {
            configFileStream.close();
        }
    }
}