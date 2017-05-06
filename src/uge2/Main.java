package uge2;

import uge1.Modulo;
import uge1.RSA;

import java.math.BigInteger;

/**
 * Created by ander on 24-04-2017.
 */
public class Main {
    public static void main(String[] args) {

        // start tid
        BigInteger message = new BigInteger("1000000000000000");


        int n = 80000;

        while(message.bitLength() < n) {
            message = message.multiply(message);
        }



        Double s = (double)System.currentTimeMillis();

        BigInteger hashValue = Signature.digestAsBigInt(message);

        Double t = (double)(System.currentTimeMillis()-s)/1000;

        System.out.println("hashing took " + t + " for " + n + "bits which is " + (n/t) + " b/s");

        //  measure to produce an rsa signature

        Modulo m = RSA.keyGen(2000);

        s = (double)System.currentTimeMillis();
        Signature.createSignature(hashValue,m);
        double timeRsa = ((double) System.currentTimeMillis() - s) / 1000;
        double timeRsaBitsPerSecond = 2000/timeRsa;

        System.out.println("It took " + timeRsa + " seconds to produce signature from hashvalue at a rate of " + timeRsaBitsPerSecond + " b/s");

        System.out.println("If we were to use RSA for the message instead of only the hashvalue, it would take: " + timeRsa*40 + " seconds at that rate.");

        System.out.println("From this we can see that using RSA on the entire message is " + ((timeRsa*40)-(t+timeRsa)) + " seconds slower than hashing and using RSA on the hashvalue.");
    }
}
