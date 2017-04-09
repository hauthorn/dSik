package uge1;

import java.math.BigInteger;
import java.util.Random;

/**
 * RSA key generation, encryption and decryption based on the BigInteger class.
 */
public class RSA {
    /**
     * The public exponent is 3 as required by the exercise
     */
    public static final BigInteger e = new BigInteger("3");


    public static Modulo keyGen(int k) {
        Modulo m = new Modulo();
        m.q = new BigInteger(k/2, new Random());
        m.p = new BigInteger(k/2, new Random());
        m.n = m.q.multiply(m.p);

        BigInteger one = new BigInteger("1");

        // Keep trying until the relation holds
        while(!(m.n.bitLength() == k && e.gcd(m.q.subtract(one)).equals(one) && e.gcd(m.p.subtract(one)).equals(one))) {
            m.q = new BigInteger(k/2, new Random());
            m.p = new BigInteger(k/2, new Random());
            m.n = m.q.multiply(m.p);
        }

        m.d = RSA.e.modPow(new BigInteger("-1"), (m.p.subtract(one)).multiply((m.q.subtract(one))));

        return m;
    }

    public static BigInteger encrypt(BigInteger plainText, Modulo m) {
        return plainText.pow(RSA.e.intValue()).mod(m.n);
    }

    public static BigInteger decrypt(BigInteger cipher, Modulo m) {
        return cipher.pow(m.d.intValue()).mod(m.n);
    }
}
