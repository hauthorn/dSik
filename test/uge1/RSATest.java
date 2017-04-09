package uge1;

import org.junit.Test;

import java.math.BigInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by Christoffer on 2017-04-09.
 */
public class RSATest {
    @Test
    public void shouldCreateValidPairPQ() throws Exception {
        Modulo m = RSA.keyGen(100);
        BigInteger one = new BigInteger("1");
        assertThat(new BigInteger("3").gcd(m.q.subtract(one)), is(one));
        assertThat(new BigInteger("3").gcd(m.p.subtract(one)), is(one));
    }

    @Test
    public void shouldCreateValidN() throws Exception {
        Modulo m = RSA.keyGen(100);
        assertThat(m.n, is(m.q.multiply(m.p)));
        assertThat(m.n.bitLength(), is(100));
    }
}