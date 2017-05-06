package uge4;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Christoffer
 * @since 2017-05-06
 */
public class KeyExchangeCommons {
    public static final BigInteger G = new BigInteger("123");
    public static final BigInteger P = new BigInteger("12345");

    public static BigInteger randomNumber() {
        Random random = new SecureRandom();
        return new BigInteger(2000, random);
    }
}
