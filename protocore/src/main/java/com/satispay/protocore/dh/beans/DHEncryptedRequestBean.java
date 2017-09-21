package com.satispay.protocore.dh.beans;

public class DHEncryptedRequestBean {

    private String userKeyId;
    private int sequenceNumber;
    private String performanceDevice;
    private String encryptedPayload;
    private String hmac;

    public String getUserKeyId() {
        return userKeyId;
    }

    public void setUserKeyId(String userKeyId) {
        this.userKeyId = userKeyId;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getPerformanceDevice() {
        return performanceDevice;
    }

    public void setPerformanceDevice(String performanceDevice) {
        this.performanceDevice = performanceDevice;
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

    public DHEncryptedRequestBean(String userKeyId, int sequenceNumber, String performanceDevice, String encryptedPayload, String hmac) {
        this.userKeyId = userKeyId;
        this.sequenceNumber = sequenceNumber;
        this.performanceDevice = performanceDevice;
        this.encryptedPayload = encryptedPayload;
        this.hmac = hmac;
    }

    public DHEncryptedRequestBean() {
    }

}
