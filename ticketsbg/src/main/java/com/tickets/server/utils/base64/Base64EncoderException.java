package com.tickets.server.utils.base64;

/**
 *
 * @author Miro
 * @version 1.0
 */
public class Base64EncoderException
    extends Exception
{

    public Base64EncoderException(Throwable cause)
    {
        super("Base64Encoder exception", cause);
    }
}
