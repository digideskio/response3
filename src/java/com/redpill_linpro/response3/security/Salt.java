package com.redpill_linpro.response3.security;

import java.math.BigInteger;
import java.security.SecureRandom;

public enum Salt {
    INSTANCE;
    private final static SecureRandom RANDOM = new SecureRandom();

    public static String getSalt() {
        return new BigInteger(130, RANDOM).toString(32);
    }
}
