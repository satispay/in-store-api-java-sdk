package com.satispay.protocore.dh.beans;

public class TokenVerificationRequestBean {

    private String nonceInc;
    private String token;
    private String ksafeServer;

    public TokenVerificationRequestBean(String nonceInc, String token, String ksafeServer) {
        this.nonceInc = nonceInc;
        this.token = token;
        this.ksafeServer = ksafeServer;
    }

    public TokenVerificationRequestBean() {
    }

    public String getNonceInc() {
        return nonceInc;
    }

    public void setNonceInc(String nonceInc) {
        this.nonceInc = nonceInc;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getKsafeServer() {
        return ksafeServer;
    }

    public void setKsafeServer(String ksafeServer) {
        this.ksafeServer = ksafeServer;
    }

}
