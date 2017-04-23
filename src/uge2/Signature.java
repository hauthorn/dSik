package uge2;

import uge1.Modulo;

import java.math.BigInteger;

/**
 * Hashes and applies a signature to a message
 */
public class Signature {
    public static BigInteger createSignature(Modulo modulo) {
        return BigInteger.ONE;
    }

    public static boolean verifySignature(BigInteger message, BigInteger signature, Modulo modulo) {
        return false;
    }
}

