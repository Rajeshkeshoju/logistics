package com.example.logstics.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "sourcePincode",
        "destinationPincode",
        "distanceInKm"
})
public class Route extends Journey{
    private int distanceInKm;

    public int getDistanceInKm() {
        return distanceInKm;
    }

    public void setDistanceInKm(int distanceInKm) {
        this.distanceInKm = distanceInKm;
    }

    @Override
    public String toString() {
        return "Route{" +
                "distanceInKm=" + distanceInKm +
                '}';
    }
}
