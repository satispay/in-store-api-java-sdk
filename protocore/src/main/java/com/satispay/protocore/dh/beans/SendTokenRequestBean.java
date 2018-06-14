package com.satispay.protocore.dh.beans;

public class SendTokenRequestBean {
    private String languageIsoCode;
    private String channel;

    public SendTokenRequestBean(String languageIsoCode, String channel) {
        this.languageIsoCode = languageIsoCode;
        this.channel = channel;
    }

    public String getLanguageIsoCode() {
        return languageIsoCode;
    }

    public void setLanguageIsoCode(String languageIsoCode) {
        this.languageIsoCode = languageIsoCode;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
