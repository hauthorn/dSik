package uge1;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Represents the collection of bigintegers needed in RSA.
 */
public class Modulo implements Serializable {
    public BigInteger n, p, q, d;
}
