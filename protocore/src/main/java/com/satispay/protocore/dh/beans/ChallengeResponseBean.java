package com.satispay.protocore.dh.beans;

public class ChallengeResponseBean {

    private String challengeResponse;
    private String nonce;

    public ChallengeResponseBean(String challenge, String nonce) {
        this.challengeResponse = challenge;
        this.nonce = nonce;
    }

    public ChallengeResponseBean() {
    }

    public String getChallengeResponse() {
        return challengeResponse;
    }

    public void setChallenge(String challenge) {
        this.challengeResponse = challenge;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }
}
