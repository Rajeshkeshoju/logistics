package com.example.logstics.model;

import java.util.Objects;

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
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TollPlazaCsv that = (TollPlazaCsv) o;
        return Double.compare(longitude, that.longitude) == 0 && Double.compare(latitude, that.latitude) == 0 && Objects.equals(tollName, that.tollName) && Objects.equals(geoState, that.geoState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude, tollName, geoState);
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
