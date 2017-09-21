package com.satispay.protocore.dh.beans;

public class TokenVerificationResponseBean {

    private String response;

    public TokenVerificationResponseBean(String response) {
        this.response = response;
    }

    public TokenVerificationResponseBean() {
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
