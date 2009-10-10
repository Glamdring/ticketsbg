package com.tickets.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.tickets.utils.base64.Base64Encoder;

public final class SecurityUtils {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Calculates a HmacSHA1 value
     * @param data
     * @param key
     * @return HmacSHA1
     */
    public static String hmac(String data, String key) {
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(),
                    HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes());

            String result = Base64Encoder.toBase64String(rawHmac);
            return result;
        } catch (Exception ex) {
            throw new RuntimeException("Problem with calculating hmac", ex);
        }
    }
}
