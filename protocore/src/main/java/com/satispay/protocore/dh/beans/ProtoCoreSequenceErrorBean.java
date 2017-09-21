package com.satispay.protocore.dh.beans;

public class ProtoCoreSequenceErrorBean {

    private int code;
    private String message;
    private String wlt;

    public ProtoCoreSequenceErrorBean(int code, String message, String wlt) {
        this.code = code;
        this.message = message;
        this.wlt = wlt;
    }

    public ProtoCoreSequenceErrorBean() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getWlt() {
        return wlt;
    }

    public void setWlt(String wlt) {
        this.wlt = wlt;
    }

}
