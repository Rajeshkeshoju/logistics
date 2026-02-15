package com.example.logstics.model;

public class Postal {
    private String circleName;
    private String regionName;
    private String divisionName;
    private String officeName;
    private int pinCode;
    private String officeType;
    private String delivery;
    private String district;
    private String stateName;
    private double latitude;
    private double longitude;

    public Postal() {}

    public Postal(String circleName, String regionName, String divisionName, String officeName, int pinCode, String officeType, String delivery, String district, String stateName, double latitude, double longitude) {
        this.circleName = circleName;
        this.regionName = regionName;
        this.divisionName = divisionName;
        this.officeName = officeName;
        this.pinCode = pinCode;
        this.officeType = officeType;
        this.delivery = delivery;
        this.district = district;
        this.stateName = stateName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getOfficeType() {
        return officeType;
    }

    public void setOfficeType(String officeType) {
        this.officeType = officeType;
    }

    public String getDelivery() {
        return delivery;
    }

    public void setDelivery(String delivery) {
        this.delivery = delivery;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Postal{" +
                "circleName='" + circleName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", divisionName='" + divisionName + '\'' +
                ", officeName='" + officeName + '\'' +
                ", pinCode=" + pinCode +
                ", officeType='" + officeType + '\'' +
                ", delivery='" + delivery + '\'' +
                ", district='" + district + '\'' +
                ", stateName='" + stateName + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
