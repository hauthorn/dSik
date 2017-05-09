package uge4;

import uge1.Modulo;
import uge1.RSA;

import java.math.BigInteger;

/**
 * Base class for Server and Client
 *
 * @author Christoffer
 * @since 2017-05-06
 */
public class Base {

    protected Modulo keyPair;
    protected PublicKey publicKey;
    protected BigInteger toSend, key, messageSoFar;

    protected void generatePublicKey() {
        keyPair = RSA.keyGen(2000);
        publicKey = new PublicKey(keyPair);
    }
}
