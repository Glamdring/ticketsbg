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
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class TicketsNotifier {

    protected static final String FIRM_KEY_SETTING = "firm.key";
    protected static final String CONFIG_FILE_NAME = "ticketsNotifierSettings.config";

    private Properties settings = new Properties();

    private String firmKey;

    public static void main(String[] args) throws IOException {
        new TicketsNotifier();
    }

    public TicketsNotifier() throws IOException {
        try {
            loadSettings();
        } catch (IOException ex) {
            //ignore the case when there is no config file
        }
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