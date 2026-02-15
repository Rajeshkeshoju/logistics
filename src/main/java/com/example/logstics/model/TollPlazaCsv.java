package com.example.logstics.model;

public class TollPlazaCsv {
    private double longitude;
    private double latitude;
    private String tollName;
    private String geoState;

    public TollPlazaCsv() {}

    public TollPlazaCsv(double longitude, double latitude, String tollName, String geoState) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.tollName = tollName;
        this.geoState = geoState;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getTollName() {
        return tollName;
    }

    public void setTollName(String tollName) {
        this.tollName = tollName;
    }

    public String getGeoState() {
        return geoState;
    }

    public void setGeoState(String geoState) {
        this.geoState = geoState;
    }

    @Override
    public String toString() {
        return "TollPlazaCsv{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", tollName='" + tollName + '\'' +
                ", geoState='" + geoState + '\'' +
                '}';
    }
}
