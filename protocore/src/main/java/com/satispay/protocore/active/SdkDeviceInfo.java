package com.satispay.protocore.active;

/**
 * Fill it with the correct values: https://s3-eu-west-1.amazonaws.com/docs.satispay.com/index.html#get-transaction-history
 * <p>
 * Please note: for .NET and Java libraries, always set “satispay-devicetype” = "CASH-REGISTER"
 * For AppB use “TABLET” / “SMARTPHONE” / “PC”
 */
public class SdkDeviceInfo {
    public enum DeviceType {
        CASH_REGISTER("CASH-REGISTER"),
        PC("PC");

        private String deviceName;

        DeviceType(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceName() {
            return deviceName;
        }
    }

    String deviceType;
    final String deviceInfo;
    final String osName;
    final String osVersion;
    final String appHouse;
    final String appName;
    final String appVersion;
    final String trackingCode;

    public SdkDeviceInfo(String deviceInfo, String osName, String osVersion, String appHouse, String appName, String appVersion, String trackingCode) {
        this(deviceInfo, osName, osVersion, appHouse, appName, appVersion, trackingCode, DeviceType.CASH_REGISTER);
    }

    public SdkDeviceInfo(String deviceInfo, String osName, String osVersion, String appHouse, String appName, String appVersion, String trackingCode, DeviceType deviceType) {
        this.deviceType = deviceType.getDeviceName();
        this.deviceInfo = deviceInfo;
        this.osName = osName;
        this.osVersion = osVersion;
        this.appHouse = appHouse;
        this.appName = appName;
        this.appVersion = appVersion;
        this.trackingCode = trackingCode;
    }

    @Deprecated
    public SdkDeviceInfo(String __unused__, String deviceInfo, String osName, String osVersion, String appHouse, String appName, String appVersion, String trackingCode) {
        this.deviceType = DeviceType.CASH_REGISTER.getDeviceName();
        this.deviceInfo = deviceInfo;
        this.osName = osName;
        this.osVersion = osVersion;
        this.appHouse = appHouse;
        this.appName = appName;
        this.appVersion = appVersion;
        this.trackingCode = trackingCode;
    }

    public void forceSetDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
