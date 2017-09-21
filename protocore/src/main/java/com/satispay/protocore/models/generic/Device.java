package com.satispay.protocore.models.generic;

public class Device extends Model {
    private boolean enable;
    private boolean mustGeolocate;

    public Device() {
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isMustGeolocate() {
        return mustGeolocate;
    }

    public void setMustGeolocate(boolean mustGeolocate) {
        this.mustGeolocate = mustGeolocate;
    }

}
