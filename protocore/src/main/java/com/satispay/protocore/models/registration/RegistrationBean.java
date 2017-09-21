package com.satispay.protocore.models.registration;

public class RegistrationBean {
    private String environment;
    private String token;
    private String osApp;
    private String osVersion;
    private String appVersion;

    public RegistrationBean(String environment, String token, String osApp, String osVersion, String appVersion) {
        this.environment = environment;
        this.token = token;
        this.osApp = osApp;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
    }

    public RegistrationBean() {
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getOsApp() {
        return osApp;
    }

    public void setOsApp(String osApp) {
        this.osApp = osApp;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

}
