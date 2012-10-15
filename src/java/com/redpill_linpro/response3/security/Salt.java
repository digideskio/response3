package com.redpill_linpro.response3.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Salt {

    public static String getSalt() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
