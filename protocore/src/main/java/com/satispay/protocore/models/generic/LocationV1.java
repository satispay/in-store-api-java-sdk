package com.satispay.protocore.models.generic;

public class LocationV1 extends Model {

    private double lat;
    private double lon;

    public LocationV1() { }

    public LocationV1(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() { return lat; }

    public void setLat(double lat) { this.lat = lat; }

    public double getLon() { return lon; }

    public void setLon(double lon) { this.lon = lon; }
}
