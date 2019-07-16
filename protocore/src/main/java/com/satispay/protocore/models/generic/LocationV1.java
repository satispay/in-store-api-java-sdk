package com.satispay.protocore.models.generic;

public class LocationV1 extends Model {

    private float lat;
    private float lon;

    public LocationV1() { }

    public LocationV1(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public float getLat() { return lat; }

    public void setLat(float lat) { this.lat = lat; }

    public float getLon() { return lon; }

    public void setLon(float lon) { this.lon = lon; }
}
