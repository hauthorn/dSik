package uge1;

import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Christoffer on 2017-04-09.
 */
public class RSATest {
    private Modulo m;
    private final int k = 1024;

    @Before
    public void setUp() throws Exception {
        m = RSA.keyGen(k);
    }

    @Test
    public void shouldCreateValidPairPQ() throws Exception {
        assertThat(RSA.e.gcd(m.q.subtract(BigInteger.ONE)), is(BigInteger.ONE));
        assertThat(RSA.e.gcd(m.p.subtract(BigInteger.ONE)), is(BigInteger.ONE));
    }

    @Test
    public void shouldCreateValidN() throws Exception {
        assertThat(m.n, is(m.q.multiply(m.p)));
        assertThat(m.n.bitLength(), is(k));
    }

    @Test
    public void shouldCreateValidD() throws Exception {
        BigInteger condition = (RSA.e.multiply(m.d)).mod(m.p.subtract(BigInteger.ONE).multiply(m.q.subtract(BigInteger.ONE)));
        assertThat(condition, is(BigInteger.ONE));
    }

    @Test
    public void shouldEncrypt() throws Exception {
        BigInteger plainText = new BigInteger("42");
        assertThat(RSA.encrypt(plainText, m), is(plainText.modPow(RSA.e, m.n)));
    }

    @Test
    public void shouldDecrypt() throws Exception {
        BigInteger cipher = new BigInteger("42");
        assertThat(RSA.decrypt(cipher, m), is(cipher.modPow(m.d, m.n)));
    }

    @Test
    public void shouldGetBackPlaintext() throws Exception {
        BigInteger plainText = new BigInteger("42");
        BigInteger cipher = RSA.encrypt(plainText, m);
        assertThat(RSA.decrypt(cipher, m), is(plainText));

        plainText = new BigInteger("12345678");
        cipher = RSA.encrypt(plainText, m);
        assertThat(RSA.decrypt(cipher, m), is(plainText));
    }
}