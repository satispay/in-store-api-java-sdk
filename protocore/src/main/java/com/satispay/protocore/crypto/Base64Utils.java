package com.satispay.protocore.crypto;

import java.io.IOException;

public class Base64Utils {
    public static String encode(byte[] source) {
        return Base64.encodeBytes(source);
    }

    public static byte[] decode(String string) {
        try {
            return Base64.decode(string);
        } catch (IOException e) {
            throw new RuntimeException("Cannot decode: " + string, e);
        }
    }
}
