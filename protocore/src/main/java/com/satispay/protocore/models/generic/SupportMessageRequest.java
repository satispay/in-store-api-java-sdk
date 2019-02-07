package com.satispay.protocore.models.generic;

public class SupportMessageRequest {
    private String message;
    private String debugMessage;
    private String reason;
    private Device device;

    public static final String REASON_SUPPORT_REQUEST = "SUPPORT_REQUEST";

    public SupportMessageRequest() {
    }

    /**
     *
     * @param message The support message
     * @param debugMessage Message sent only in internal mail
     * @param reason Support message reason, possible values [SUPPORT_REQUEST]
     * @param device required
     */
    public SupportMessageRequest(String message, String debugMessage, String reason, Device device) {
        this.message = message;
        this.debugMessage = debugMessage;
        this.reason = reason;
        this.device = device;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public static class Device {
        private String os;
        private String osVersion;
        private String modelCode;
        private String producer;
        private String appVersion;
        private String appRelease;

        public Device() {
        }

        public Device(String os, String osVersion, String modelCode, String producer, String appVersion, String appRelease) {
            this.os = os;
            this.osVersion = osVersion;
            this.modelCode = modelCode;
            this.producer = producer;
            this.appVersion = appVersion;
            this.appRelease = appRelease;
        }

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public String getModelCode() {
            return modelCode;
        }

        public void setModelCode(String modelCode) {
            this.modelCode = modelCode;
        }

        public String getProducer() {
            return producer;
        }

        public void setProducer(String producer) {
            this.producer = producer;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getAppRelease() {
            return appRelease;
        }

        public void setAppRelease(String appRelease) {
            this.appRelease = appRelease;
        }
    }
}
