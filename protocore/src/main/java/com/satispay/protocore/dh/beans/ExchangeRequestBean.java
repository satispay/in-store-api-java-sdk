package com.satispay.protocore.dh.beans;

public class ExchangeRequestBean {

    private String p;
    private String g;
    private String publicKey;
    private String languageIso6391;
    private String countryIso3166;

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLanguageIso6391() {
        return languageIso6391;
    }

    public void setLanguageIso6391(String languageIso6391) {
        this.languageIso6391 = languageIso6391;
    }

    public String getCountryIso3166() {
        return countryIso3166;
    }

    public void setCountryIso3166(String countryIso3166) {
        this.countryIso3166 = countryIso3166;
    }

    public ExchangeRequestBean(String p, String g, String publicKey, String languageIso6391, String countryIso3166) {
        this.p = p;
        this.g = g;
        this.publicKey = publicKey;
        this.languageIso6391 = languageIso6391;
        this.countryIso3166 = countryIso3166;
    }

    public ExchangeRequestBean() {
    }

}
