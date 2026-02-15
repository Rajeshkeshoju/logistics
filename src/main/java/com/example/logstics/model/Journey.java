package com.example.logstics.model;

public class Journey {
    private String sourcePincode;
    private String destinationPincode;

    public String getSourcePincode() {
        return sourcePincode;
    }

    public void setSourcePincode(String sourcePincode) {
        this.sourcePincode = sourcePincode;
    }

    public String getDestinationPincode() {
        return destinationPincode;
    }

    public void setDestinationPincode(String destinationPincode) {
        this.destinationPincode = destinationPincode;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "sourcePincode='" + sourcePincode + '\'' +
                ", destinationPincode='" + destinationPincode + '\'' +
                '}';
    }
}
