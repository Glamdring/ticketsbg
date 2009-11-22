package com.tickets.test;

import java.security.SecureRandom;
import java.util.Random;

public final class TestUtils {
    private static Random r = new SecureRandom();

    /**
     * Nobody wants to make it concrete
     */
    private TestUtils(){

    }

    /**
     * Get random string with a given length
     * @param charactersLength
     * @return a random string of the specified length
     */
    public static String getRandomString(int charactersLength) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < charactersLength; i++) {
            int randInt = r.nextInt(Character.MAX_RADIX);
            b.append(Integer.toString(randInt, Character.MAX_RADIX));
        }
        return b.toString();
    }

    /**
     * Return random positive integer between 0 and mod-1 inclusive
     * @param mod
     * @return int
     */
    public static int nextInteger(int mod){
        return r.nextInt(mod);
    }


    /**
     * Creates a random string, which is a valid email
     *
     * @return the generated email
     */
    public static String getRandomEmail() {
        return getRandomString(8) + "@" + getRandomString(7) + ".com";
    }

}
