package com.satispay.protocore.models.analytics;

public class AppStartedBean {
    public AppStartedBean() {
    }

    private String osApp;
    private String osVersion;
    private String appVersion;
    private String deviceModel;
    private String language;
    private double geoLat;
    private double geoLng;

    public String getOsApp() {
        return osApp;
    }

    public AppStartedBean(String osApp, String osVersion, String appVersion, String deviceModel, String language, double geoLat, double geoLng) {
        this.osApp = osApp;
        this.osVersion = osVersion;
        this.appVersion = appVersion;
        this.deviceModel = deviceModel;
        this.language = language;
        this.geoLat = geoLat;
        this.geoLng = geoLng;
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

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public double getGeoLat() {
        return geoLat;
    }

    public void setGeoLat(double geoLat) {
        this.geoLat = geoLat;
    }

    public double getGeoLng() {
        return geoLng;
    }

    public void setGeoLng(double geoLng) {
        this.geoLng = geoLng;
    }

}
