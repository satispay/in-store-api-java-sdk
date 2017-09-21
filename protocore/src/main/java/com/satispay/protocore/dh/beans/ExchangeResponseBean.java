package com.satispay.protocore.dh.beans;

import java.math.BigInteger;
import java.util.Date;

public class ExchangeResponseBean {

    private String userKeyId;
    private BigInteger publicKey;
    private Date expirationDate;

    public ExchangeResponseBean(String userKeyId, BigInteger publicKey, Date expirationDate) {
        this.userKeyId = userKeyId;
        this.publicKey = publicKey;
        this.expirationDate = expirationDate;
    }

    public ExchangeResponseBean() {
    }

    public String getUserKeyId() {
        return userKeyId;
    }

    public void setUserKeyId(String userKeyId) {
        this.userKeyId = userKeyId;
    }

    public BigInteger getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(BigInteger publicKey) {
        this.publicKey = publicKey;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

}
