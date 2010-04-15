package com.tickets.utils.timers;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.tickets.constants.Settings;
import com.tickets.utils.GeneralUtils;

public class BackupTask extends TimerTask {

    private static final Logger log = Logger.getLogger(BackupTask.class);

    @Autowired
    private ComboPooledDataSource dataSource;

    private boolean performBackup;

    @Override
    public void run() {
        if (performBackup) {
            parseAndPerformBackup(dataSource.getUser(), dataSource.getPassword(),
                    dataSource.getJdbcUrl());
        }
    }

    private void parseAndPerformBackup(String user, String password, String jdbcUrl) {
        String port = "3306";

        Pattern hostPattern = Pattern.compile("//((\\w)+)/");
        Matcher m = hostPattern.matcher(jdbcUrl);
        String host = null;
        if (m.find()) {
            host = m.group(1);
        }

        Pattern dbPattern = Pattern.compile("/((\\w)+)\\?");
        m = dbPattern.matcher(jdbcUrl);
        String db = null;
        if (m.find()) {
            db = m.group(1);
        }

        log.debug(host + ":" + port + ":" + user + ":" + password + ":" + db);

        try {
            createBackup(host, port, user, password, db);
        } catch (Exception ex) {
            log.error("Error during backup", ex);
        }

    }

    public static void main(String[] args) throws Exception {
        BackupTask bt = new BackupTask();

        bt.parseAndPerformBackup("root", "",
                "jdbc:mysql://localhost/avtogara?characterEncoding=utf8");

    }

    private void createBackup(String host, String port, String user, String password,
            String db) throws Exception {

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        String fileName = Settings.getValue("backup.dir") + "/backup-"
                + df.format(GeneralUtils.createCalendar().getTime()) + ".sql";

        String execString = "mysqldump --host=" + host + " --port=" + port + " --user="
                + user + " --password=" + password
                + " --compact --complete-insert --extended-insert "
                + "--skip-comments --skip-triggers --default-character-set=utf8 " + db
                + " --result-file=" + fileName;

        Process process = Runtime.getRuntime().exec(execString);
        if (process.waitFor() == 0) {
            zipBackup(fileName);
        }
        // result = "SET FOREIGN_KEY_CHECKS = 0;\\n" + result
        // + "\\nSET FOREIGN_KEY_CHECKS = 1;";
    }

    private static final int BUFFER = 2048;

    private void zipBackup(String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName.replace(".sql", "") + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));

        File entryFile = new File(fileName);
        FileInputStream fi = new FileInputStream(entryFile);
        InputStream origin = new BufferedInputStream(fi, BUFFER);
        ZipEntry entry = new ZipEntry("data.sql");
        zos.putNextEntry(entry);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            zos.write(data, 0, count);
        }
        origin.close();
        zos.close();

        entryFile.delete();
    }

    public boolean isPerformBackup() {
        return performBackup;
    }

    public void setPerformBackup(boolean performBackup) {
        this.performBackup = performBackup;
    }
}
