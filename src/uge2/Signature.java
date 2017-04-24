package uge2;

import uge1.Modulo;
import uge1.RSA;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes and applies a signature to a message.
 *
 * Uses the {@link RSA} class.
 */
public class Signature {
    public static BigInteger createSignature(BigInteger message, Modulo modulo) {
        return RSA.decrypt(digestAsBigInt(message), modulo);
    }

    public static boolean verifySignature(BigInteger message, BigInteger signature, Modulo modulo) {
        return RSA.encrypt(signature, modulo).equals(digestAsBigInt(message));
    }

    public static BigInteger digestAsBigInt(BigInteger message) {
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            return new BigInteger(1, instance.digest(message.toByteArray()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Digest went wrong.");
    }
}

