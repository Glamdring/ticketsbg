package com.tickets.utils.base64;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

/**
 * 
 * @author Miro
 * @version 1.0
 */
public class Base64Decoder
{

    private static final int BASELENGTH = 255;
    private static final int BUFFER_SIZE = 1024;
    private static final byte PAD = (byte)'=';
    private static final int b00111111x3F = 0x3F;
    private static final int b00110000x30 = 0x30;
    private static final int b00001111x0F = 0x0F;
    private static final int b00111100x3C = 0x3C;
    private static final int b00000011x03 = 0x03;
    private static final byte[] base64Alphabet = new byte[BASELENGTH];
    

    static
    {
        for(int i = 0; i < BASELENGTH; i++)
        {
            base64Alphabet[i] = (byte) - 1;
        }
        for(int i = 'Z'; i >= 'A'; i--)
        {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for(int i = 'z'; i >= 'a'; i--)
        {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }
        for(int i = '9'; i >= '0'; i--)
        {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;
    }
    private InputStream base64Stream;
    private OutputStream pureDataStream;

    private byte[] discardWhitespace(byte[] data, int length)
        throws Base64FormatException
    {
        byte pureData[] = new byte[length];
        int bytesCopied = 0;
        byte byteValue;

        for(int i = 0; i < length; i++)
        {
            byteValue = data[i];
            if(!Character.isWhitespace(byteValue) &&
                !Character.isSpaceChar(byteValue))
            {
                if(byteValue == PAD || base64Alphabet[byteValue] != -1)
                {
                    pureData[bytesCopied++] = data[i];
                }
                else
                {
                    throw new Base64FormatException("ERROR: Invalid charater (byte): '" +
                        (char) byteValue + " (" + byteValue + ") in Base64 stream.");
                }
            }
        }

        byte packedData[] = new byte[bytesCopied];

        System.arraycopy(pureData, 0, packedData, 0, bytesCopied);

        return packedData;
    }

    private final int getByteValue(int charValue)
    {
        if(charValue >= 'A' && charValue <= 'Z')
        {
            return charValue - 'A';
        }
        else if(charValue >= 'a' && charValue <= 'z')
        {
            return charValue - 'a' + 26;
        }
        else if(charValue >= '0' && charValue <= '9')
        {
            return charValue - '0' + 52;
        }
        switch(charValue)
        {
            case '=':
                return PAD;
            case '+':
                return 62;
            case '/':
                return 63;
            default:
                return -1;
        }
    }

    public void decode()
        throws IOException,
        Base64FormatException
    {
        byte buffer[] = new byte[BUFFER_SIZE];
        int read;
        int pos = 0;
        int byteValue = 0;
        int offset = 0;
        byte fourBytes[] = new byte[4];

        while ((read = base64Stream.read(buffer)) >= 0)
        {
            byte base64Data[] = discardWhitespace(buffer, read);
            read = base64Data.length;
            offset = 0;
            while (offset < read)
            {
                while (pos < 4 && offset < read)
                {
                    fourBytes[pos++] = base64Data[offset++];
                }
                if(pos >= 4)
                {
                    pos = 0;
                    byteValue = (getByteValue(fourBytes[0]) & b00111111x3F) << 2;
                    byteValue |= (getByteValue(fourBytes[1]) & b00110000x30) >>> 4;
                    pureDataStream.write(byteValue);
                    if(fourBytes[2] == PAD)
                    {
                        break;
                    }
                    byteValue = (getByteValue(fourBytes[1]) & b00001111x0F) << 4;
                    byteValue |= (getByteValue(fourBytes[2]) & b00111100x3C) >>> 2;
                    pureDataStream.write(byteValue);
                    if(fourBytes[3] == PAD)
                    {
                        break;
                    }
                    byteValue = (getByteValue(fourBytes[2]) & b00000011x03) << 6;
                    byteValue |= (getByteValue(fourBytes[3]) & b00111111x3F);
                    pureDataStream.write(byteValue);
                }
                Thread.yield();
            }
        }
        pureDataStream.flush();
        pureDataStream.close();
    }

    public Base64Decoder(InputStream base64Stream, OutputStream pureDataStream)
    {
        this.base64Stream = base64Stream;
        this.pureDataStream = pureDataStream;
    }

    public static byte[] toByteArray(String base64String)
        throws Base64FormatException,
        IOException
    {
        ByteArrayInputStream inStream = new ByteArrayInputStream(base64String.getBytes());
        return toByteArray(inStream);
    }

    public static byte[] toByteArray(InputStream inStream)
        throws Base64FormatException,
        IOException
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Base64Decoder base64 = new Base64Decoder(inStream, outStream);
        base64.decode();
        return outStream.toByteArray();
    }

    static public class Base64DecoderThread
        implements Runnable
    {

        private Base64Decoder base64;
        private Throwable throwable = null;
        private boolean isWorking = false;
        private boolean waitClause = false;

        public Base64DecoderThread(InputStream base64Stream, OutputStream pureDataStream)
        {
            base64 = new Base64Decoder(base64Stream, pureDataStream);
        }

        @Override
        public void run()
        {
            try
            {
                isWorking = true;
                base64.decode();
                while (waitClause)
                {
                    Thread.yield();
                }
            }
            catch(Throwable ex)
            {
                throwable = ex;
                /*Logger errorLogger = getLoggerHelper().getErrorLogger();
                if(errorLogger != null)
                {
                    errorLogger.logp(Level.SEVERE, this.getClass().getName(), "run()", "waitClause = " + waitClause, ex);
                }*/
            }
            finally
            {
                isWorking = false;
            }
        }

        public Throwable getThrowable()
        {
            return throwable;
        }

        public boolean isWorking()
        {
            return isWorking;
        }

        public void setWaitClause(boolean flag)
        {
            waitClause = flag;
        }

        public boolean getWaitClause()
        {
            return waitClause;
        }
    }
}
