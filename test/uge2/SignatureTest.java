package uge2;

import org.junit.Test;
import uge1.Modulo;
import uge1.RSA;

import java.math.BigInteger;

import static org.junit.Assert.*;

/**
 * Tests the Signature class as required by hand in 2.
 */
public class SignatureTest {
    @Test
    public void canVerifySignature() throws Exception {
        BigInteger message = new BigInteger("42");
        Modulo modulo = RSA.keyGen(2000);

        BigInteger signature = Signature.createSignature(message, modulo);

        assertTrue(Signature.verifySignature(message, signature, modulo));

        // Modify the message, expect verification to fail
        message = message.add(BigInteger.ONE);
        assertFalse(Signature.verifySignature(message, signature, modulo));
    }
}