package uge4;

import uge1.Modulo;
import uge1.RSA;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents a public key
 *
 * @author Christoffer
 * @since 2017-05-06
 */
public class PublicKey implements Serializable {
    public BigInteger n, e;

    public PublicKey(Modulo modulo) {
        this.n = modulo.n;
        this.e = RSA.e;
    }
}
