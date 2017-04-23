package uge2;

import uge1.Modulo;
import uge1.RSA;

import java.math.BigInteger;

/**
 * Hashes and applies a signature to a message
 */
public class Signature {
    public static BigInteger createSignature(BigInteger message, Modulo modulo) {
        return RSA.decrypt(message, modulo);
    }

    public static boolean verifySignature(BigInteger message, BigInteger signature, Modulo modulo) {
        return RSA.encrypt(signature, modulo).equals(message);
    }
}

