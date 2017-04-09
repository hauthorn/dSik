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
    private final int k = 20;

    @Before
    public void setUp() throws Exception {
        m = RSA.keyGen(k);
    }

    @Test
    public void shouldCreateValidPairPQ() throws Exception {
        BigInteger one = new BigInteger("1");
        assertThat(RSA.e.gcd(m.q.subtract(one)), is(one));
        assertThat(RSA.e.gcd(m.p.subtract(one)), is(one));
    }

    @Test
    public void shouldCreateValidN() throws Exception {
        assertThat(m.n, is(m.q.multiply(m.p)));
        assertThat(m.n.bitLength(), is(k));
    }

    @Test
    public void shouldCreateValidD() throws Exception {
        BigInteger one = new BigInteger("1");
        BigInteger expected = RSA.e.mod((m.p.subtract(one)).multiply((m.q.subtract(one))));
        assertThat(m.d, is(expected));
    }

    @Test
    public void shouldEncrypt() throws Exception {
        BigInteger plainText = new BigInteger("42");
        assertThat(RSA.encrypt(plainText, m), is(plainText.pow(RSA.e.intValue()).mod(m.n)));
    }

    @Test
    public void shouldDecrypt() throws Exception {
        BigInteger cipher = new BigInteger("42");
        assertThat(RSA.decrypt(cipher, m), is(cipher.pow(m.d.intValue()).mod(m.n)));
    }

    @Test
    public void shouldGetBackPlaintext() throws Exception {
        BigInteger plainText = new BigInteger("42");

        assertThat(RSA.decrypt(RSA.encrypt(plainText, m), m), is(plainText));
    }
}