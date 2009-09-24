package com.tickets.utils.base64;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author Miro
 * @version 1.0
 */
public class Base64Encoder {

    private static final int CHUNK_SIZE = 76;
    private static byte[] CHUNK_SEPARATOR = "\n".getBytes();
    private static final int LOOKUPLENGTH = 64 + 1;
    // private static final int SIGN = -128;
    // private static final int BUFFER_SIZE = 16;
    private static final int BUFFER_SIZE = 1024;
    private static final byte PAD = (byte) '=';

    // Create arrays to hold the base64 characters and a
    private static final int PAD_POSITION = LOOKUPLENGTH - 1;

    // lookup for base64 chars
    private static final byte[] lookUpBase64Alphabet = new byte[LOOKUPLENGTH];

    // Populating the lookup and character arrays
    static {
        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (byte) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (byte) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (byte) ('0' + j);
        }

        lookUpBase64Alphabet[62] = (byte) '+';
        lookUpBase64Alphabet[63] = (byte) '/';
        lookUpBase64Alphabet[PAD_POSITION] = PAD;
    }
    private InputStream pureDataStream;
    private OutputStream base64Stream;
    private boolean isChunked;
    private int count = 0;
    private static final int b11111100xFC = 0xFC;
    private static final int b00000011x03 = 0x03;
    private static final int b11110000xF0 = 0xF0;
    private static final int b00001111x0F = 0x0F;
    private static final int b11000000xC0 = 0xC0;
    private static final int b00111111x3F = 0x3F;

    private void writeBase64(int charValue) throws IOException {
        if (isChunked && (++count) > CHUNK_SIZE) {
            base64Stream.write(CHUNK_SEPARATOR);
            count = 1;
        }
        base64Stream.write(lookUpBase64Alphabet[charValue]);
    }

    private void writeBase64(byte threeBytes[], int pos) throws IOException {
        int charValue;
        charValue = (threeBytes[0] & b11111100xFC) >> 2;
        writeBase64(charValue);

        charValue = (threeBytes[0] & b00000011x03) << 4;
        charValue |= (threeBytes[1] & b11110000xF0) >>> 4;
        writeBase64(charValue);

        if (pos == 3 || pos == 2) {
            charValue = (threeBytes[1] & b00001111x0F) << 2;
            charValue |= (threeBytes[2] & b11000000xC0) >>> 6;
            writeBase64(charValue);
        } else {
            writeBase64(PAD_POSITION);
        }
        if (pos == 3) {
            charValue = threeBytes[2] & b00111111x3F;
            writeBase64(charValue);
        } else {
            writeBase64(PAD_POSITION);
        }
    }

    public void setChunkSeparator(String chunkSeparator) {
        if (chunkSeparator != null && chunkSeparator.length() > 0) {
            setChunkSeparator(chunkSeparator.getBytes());
        }
    }

    public void setChunkSeparator(byte bytes[]) {
        CHUNK_SEPARATOR = bytes;
    }

    public void encode() throws IOException {
        byte buffer[] = new byte[BUFFER_SIZE];
        int read;
        int pos = 0;
        int offset = 0;
        byte threeBytes[] = new byte[3];

        while ((read = pureDataStream.read(buffer)) >= 0) {
            offset = 0;
            while (offset < read) {
                while (pos < 3 && offset < read) {
                    threeBytes[pos++] = buffer[offset++];
                }
                if (pos >= 3) {
                    writeBase64(threeBytes, pos);
                    pos = 0;
                }
                Thread.yield();
            }
        }
        if (pos > 0 && pos < 3) {
            writeBase64(threeBytes, pos);
        }
        base64Stream.flush();
        base64Stream.close();
    }

    public Base64Encoder(InputStream pureDataStream, OutputStream base64Stream) {
        this(pureDataStream, base64Stream, true);
    }

    public Base64Encoder(InputStream pureDataStream, OutputStream base64Stream,
            boolean isChunked) {
        this.pureDataStream = pureDataStream;
        this.base64Stream = base64Stream;
        this.isChunked = isChunked;
    }

    public static String toBase64String(byte byteArray[]) throws IOException {
        return toBase64String(byteArray, false);
    }

    public static String toBase64String(byte byteArray[], boolean isChunked)
            throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(byteArray);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream(
                (int) (byteArray.length * 1.5));
        Base64Encoder base64 = new Base64Encoder(inStream, outStream, isChunked);
        base64.encode();
        return outStream.toString();
    }

    static public class Base64EncoderThread implements Runnable {

        private Base64Encoder base64;
        private Throwable throwable = null;
        private boolean isWorking = false;
        private boolean waitClause = false;

        public Base64EncoderThread(InputStream pureDataStream,
                OutputStream base64Stream) {
            base64 = new Base64Encoder(pureDataStream, base64Stream);
        }

        public Base64EncoderThread(InputStream pureDataStream,
                OutputStream base64Stream, boolean isChunked) {
            base64 = new Base64Encoder(pureDataStream, base64Stream, isChunked);
        }

        @Override
        public void run() {
            try {
                isWorking = true;
                base64.encode();
                while (waitClause) {
                    Thread.yield();
                }
            } catch (Throwable ex) {
                throwable = ex;
                /*
                 * Logger errorLogger = getLoggerHelper().getErrorLogger();
                 * if(errorLogger != null) { errorLogger.logp(Level.SEVERE,
                 * this.getClass().getName(), "run()", "waitClause = " +
                 * waitClause, ex); }
                 */
            } finally {
                isWorking = false;
            }
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public boolean isWorking() {
            return isWorking;
        }

        public void setWaitClause(boolean flag) {
            waitClause = flag;
        }

        public boolean getWaitClause() {
            return waitClause;
        }
    }

    static byte test[] = { 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4,
            6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3,
            4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9,
            10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13,
            14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2,
            3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8,
            9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12,
            13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0,
            1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6,
            7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11,
            12, 13, 14, 15, 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
            0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12,
    // 0, 1, 2, 3, 4, 6, 7, 8, 9, 10, 11,
    // 0, 1, 2, 3, 4, 6, 7, 8, 9, 10,
    // 0, 1, 2, 3, 4, 6, 7, 8, 9,
    };

    public static boolean isByteArrayEqual(byte ba1[], byte ba2[]) {
        System.out.println("... Comparing ...");
        boolean result = true;
        int length;
        if (ba1.length != ba2.length) {
            result = false;
            if (ba1.length < ba2.length) {
                length = ba1.length;
            } else {
                length = ba2.length;
            }
        } else {
            length = ba1.length;
        }
        System.out.println("Byte Array 1 Length = " + ba1.length
                + ", Byte Array 2 Length = " + ba2.length);
        for (int i = 0; i < length; i++) {
            if (ba1[i] != ba2[i]) {
                System.out.println("Position " + (i + 1) + ", values[1:2] => "
                        + ba1[i] + " : " + ba2[i]);
            }
        }
        int diff = Math.abs(ba1.length - ba2.length);
        if (diff != 0) {
            int maxLength = length + diff;
            for (int i = 0; i < diff; i++) {
                int pos = length + i;
                System.out.print("Position " + (pos + 1) + " of " + maxLength
                        + ", values[1:2] => ");
                if (ba1.length <= pos) {
                    System.out.println(" NULL : " + ba2[pos] + " ('"
                            + (char) ba2[pos] + "')");
                } else {
                    System.out.println(ba1[pos] + " ('" + (char) ba1[pos]
                            + "') : NULL");
                }
            }
        }
        return result;
    }
}
