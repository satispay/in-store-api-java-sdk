package com.satispay.protocore.models.analytics;

public class AppStartedBean {
    public AppStartedBean() {
    }

    private String language;
    private String udid;

    public AppStartedBean(String language, String udid) {
        this.language = language;
        this.udid = udid;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }
}
