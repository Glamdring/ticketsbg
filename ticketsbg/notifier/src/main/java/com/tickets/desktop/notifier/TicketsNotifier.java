package com.tickets.desktop.notifier;

import java.awt.AWTException;
import java.awt.FlowLayout;
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
import javax.swing.JTextField;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.protocol.Protocol;

public class TicketsNotifier {

    protected static final String FIRM_KEY_SETTING = "firm.key";
    protected static final String CONFIG_FILE_NAME = "ticketsNotifierSettings.config";
    private static final long POLL_INTERVAL = 3 * 60 * 1000;

    private HttpClient httpClient;

    private Properties settings = new Properties();

    private String firmKey;

    private Timer poller;

    public static void main(String[] args) throws IOException {
        new TicketsNotifier();
    }

    public TicketsNotifier() throws IOException {
        try {
            loadSettings();
        } catch (IOException ex) {
            // ignore the case when there is no config file
        }

        poller = new Timer();

        poller.schedule(new PollerTask(getHttpClient()), 0, POLL_INTERVAL);
        firmKey = settings.getProperty(FIRM_KEY_SETTING);
        final PopupMenu popUp = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage("images/icon.png"));
        trayIcon.setImageAutoSize(true);

        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem settingsItem = new MenuItem(Messages.getString("settings"));
        settingsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsPanel sp = new SettingsPanel(firmKey);
                firmKey = sp.open();
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
    private void loadSettings() throws IOException {
        String configFileName = getConfigFileName();
        FileInputStream configFileStream = new FileInputStream(configFileName);
        try {
            settings = new Properties();
            settings.load(configFileStream);
        } finally {
            configFileStream.close();
        }
    }


    private HttpClient getHttpClient() {
        if (httpClient == null) {
            Protocol myhttps = new Protocol("https",
                    new EasySSLProtocolSocketFactory(), 443);

            httpClient = new HttpClient();

            String hostname = "tickets.bg";
            httpClient.getHostConfiguration().setHost(hostname, 443, myhttps);

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
                List<Proxy> proxyList = ps.select(new URI("https://tickets.bg/updates"));
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
                ex.printStackTrace();
            }
        }

        return httpClient;
    }

}

class SettingsPanel extends JDialog {
    private JTextField firmKeyField;
    private boolean saved = false;
    private String initialFirmKey;

    public SettingsPanel(String firmKey) {
        setResizable(false);
        setTitle(Messages.getString("settings"));
        firmKeyField = new JTextField(15);
        firmKeyField.setText(firmKey);
        initialFirmKey = firmKey;
        this.setBackground(SystemColor.control);

        getContentPane().setLayout(new FlowLayout());

        getContentPane().add(new JLabel(Messages.getString("firmKey") + ": "));
        getContentPane().add(firmKeyField);

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

    public String open() {
        setLocationRelativeTo(null);
        setModal(true);
        // blocking
        setVisible(true);

        if (!saved) {
            return initialFirmKey;
        }
        return firmKeyField.getText();
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
        configProps.setProperty(TicketsNotifier.FIRM_KEY_SETTING, firmKey);

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

class PollerTask extends TimerTask {

    //TODO listeners

    private HttpClient client;
    public PollerTask(HttpClient client) {
        this.client = client;
    }

    @Override
    public void run() {
    }

    public HttpClient getClient() {
        return client;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }
}