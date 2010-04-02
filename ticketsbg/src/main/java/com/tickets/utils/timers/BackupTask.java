package com.tickets.utils.timers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.Deflater;
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

    private void parseAndPerformBackup(String user, String password,
            String jdbcUrl) {
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
            // TODO buffer the whole process
            storeBackup(getData(host, port, user, password, db));
        } catch (Exception ex) {
            log.error("Error during backup", ex);
        }

    }

    public static void main(String[] args) throws Exception{
        BackupTask bt = new BackupTask();

        bt.parseAndPerformBackup("root", "",
                "jdbc:mysql://localhost/avtogara?characterEncoding=utf8");

    }

    private int BUFFER = 10485760;

    private String getData(String host, String port, String user,
            String password, String db) throws Exception {

        String execString = "mysqldump --host=" + host + " --port=" + port + " --user="
            + user + " --password=" + password
            + " --compact --complete-insert --extended-insert "
            + "--skip-comments --skip-triggers --default-character-set=utf8 " + db;

        Process run = Runtime.getRuntime().exec(execString);
        InputStream in = run.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        StringBuffer temp = new StringBuffer();

        int count;
        char[] cbuf = new char[BUFFER];

        while ((count = br.read(cbuf, 0, BUFFER)) != -1) {
            temp.append(cbuf, 0, count);
        }

        br.close();
        in.close();

        String result = temp.toString();

        result = "SET FOREIGN_KEY_CHECKS = 0;\\n" + result
                + "\\nSET FOREIGN_KEY_CHECKS = 0;";

        return result;
    }

    private void storeBackup(String stringData) throws IOException {
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy-HH-mm");
        byte[] data = stringData.getBytes();
        File filedst = new File(Settings.getValue("backup.dir") + "/backup-"
                + df.format(GeneralUtils.createCalendar().getTime()) + ".zip");

        FileOutputStream dest = new FileOutputStream(filedst);
        ZipOutputStream zip = new ZipOutputStream(
                new BufferedOutputStream(dest));

        zip.setMethod(ZipOutputStream.DEFLATED);
        zip.setLevel(Deflater.BEST_COMPRESSION);

        zip.putNextEntry(new ZipEntry("data.sql"));
        zip.write(data);

        zip.close();
        dest.close();
    }

    public boolean isPerformBackup() {
        return performBackup;
    }

    public void setPerformBackup(boolean performBackup) {
        this.performBackup = performBackup;
    }
}
