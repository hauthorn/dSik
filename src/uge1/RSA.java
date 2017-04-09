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
        Random random = new Random();
        m.q = new BigInteger(k/2, 100, random);
        m.p = new BigInteger(k/2, 100, random);
        m.n = m.q.multiply(m.p);

        BigInteger phi = getPhi(m);

        // Keep trying until the relation holds
        while(!(m.n.bitLength() == k && e.gcd(phi).equals(BigInteger.ONE))) {
            random = new Random();
            m.q = new BigInteger(k/2, random);
            m.p = new BigInteger(k/2, random);
            m.n = m.q.multiply(m.p);
            phi = getPhi(m);
        }

        m.d = RSA.e.modInverse(phi);

        return m;
    }

    public static BigInteger encrypt(BigInteger plainText, Modulo m) {
        return plainText.modPow(e, m.n);
    }

    public static BigInteger decrypt(BigInteger cipher, Modulo m) {
        return cipher.modPow(m.d, m.n);
    }

    private static BigInteger getPhi(Modulo m) {
        return m.q.subtract(BigInteger.ONE).multiply(m.p.subtract(BigInteger.ONE));
    }
}
