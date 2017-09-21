package com.satispay.protocore.dh.beans;

public class DHEncryptedResponseBean {

    private String encryptedPayload;
    private String hmac;

    public DHEncryptedResponseBean(String encryptedPayload, String hmac) {
        this.encryptedPayload = encryptedPayload;
        this.hmac = hmac;
    }

    public DHEncryptedResponseBean() {
    }

    public String getEncryptedPayload() {
        return encryptedPayload;
    }

    public void setEncryptedPayload(String encryptedPayload) {
        this.encryptedPayload = encryptedPayload;
    }

    public String getHmac() {
        return hmac;
    }

    public void setHmac(String hmac) {
        this.hmac = hmac;
    }

}
