package com.satispay.protocore.dh.beans;

public class ChallengeRequestBean {

    private String challenge;

    public ChallengeRequestBean(String challenge) {
        this.challenge = challenge;
    }

    public ChallengeRequestBean() {
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }

}
