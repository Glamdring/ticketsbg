package com.tickets.utils;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import com.tickets.constants.Settings;

public final class SecurityUtils {

    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    /**
     * Calculates a HmacSHA1 value
     *
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

            String result = new String(Hex.encodeHex(rawHmac));
            return result.toUpperCase();
        } catch (Exception ex) {
            throw new RuntimeException("Problem with calculating hmac", ex);
        }
    }

    public static byte[] sign(byte[] content, PrivateKey key) {
        try {
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(key);
            signature.update(content);
            return signature.sign();
        } catch (Exception ex) {
            throw new RuntimeException("Problem with signing", ex);
        }
    }

    public static boolean verify(byte[] signature, byte[] content, Certificate certificate) {
        try {
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(certificate);
            sig.update(content);
            return sig.verify(signature);
        } catch (Exception ex) {
            throw new RuntimeException("Problem with signature verification", ex);
        }
    }

    private static KeyStore boricaClientKeystore;

    public static KeyStore getBoricaClientKeyStore() {
        try {
            if (boricaClientKeystore == null) {
                boricaClientKeystore = KeyStore.getInstance("JKS");
                boricaClientKeystore.load(SecurityUtils.class
                        .getResourceAsStream(Settings
                                .getValue("borica.keystore")), Settings
                        .getValue("borica.keystore.password").toCharArray());
            }
            return boricaClientKeystore;
        } catch (Exception ex) {
            boricaClientKeystore = null;
            throw new RuntimeException("Error getting Borica client KeyStore",
                    ex);
        }
    }

    private static PrivateKey boricaClientPrivateKey;

    public static PrivateKey getBoricaClientPrivateKey() {
        try {
            if (boricaClientPrivateKey == null) {
                boricaClientPrivateKey = (PrivateKey) getBoricaClientKeyStore()
                        .getKey(Settings.getValue("borica.alias"),
                                Settings.getValue("borica.alias.password")
                                        .toCharArray());
            }

            return boricaClientPrivateKey;
        } catch (Exception ex) {
            throw new RuntimeException("Error loading private key", ex);
        }
    }

    private static Certificate boricaCertificate;

    public static Certificate getBoricaCertificate() {
        try {
            if (boricaCertificate == null) {
                InputStream is = SecurityUtils.class.getResourceAsStream(Settings.getValue("borica.certificate"));
                boricaCertificate = CertificateFactory.getInstance("X.509")
                        .generateCertificate(is);
            }

            return boricaCertificate;
        } catch (Exception ex) {
            throw new RuntimeException("Error loading Certificate key", ex);
        }
    }
}
