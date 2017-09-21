package com.satispay.protocore.models.generic;


public class VersionUpdate extends Model {

    public enum VersionStatus {ENABLED, DEPRECATED}

    private String versionStatus;

    public VersionUpdate() {
    }

    public VersionUpdate(String versionStatus) {
        this.versionStatus = versionStatus;
    }

    public String getVersionStatus() {
        return versionStatus;
    }

    public void setVersionStatus(String versionStatus) {
        this.versionStatus = versionStatus;
    }

    public boolean isDeprecated() {
        return VersionStatus.DEPRECATED.name().equals(versionStatus);
    }
}
