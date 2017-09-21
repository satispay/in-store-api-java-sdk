package com.satispay.protocore.dh;

import java.math.BigInteger;

public class DHKeys {

    private BigInteger p;
    private BigInteger g;
    private BigInteger privateKey;
    private BigInteger publicKey;

    public DHKeys() {
    }

    public DHKeys(BigInteger p, BigInteger g, BigInteger privateKey, BigInteger publicKey) {
        this.p = p;
        this.g = g;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public BigInteger getP() {
        return p;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getPrivateKey() {
        return privateKey;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setP(BigInteger p) {
        this.p = p;
    }

    public void setG(BigInteger g) {
        this.g = g;
    }

    public void setPrivateKey(BigInteger privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }
}
