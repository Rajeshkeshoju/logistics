package com.example.logstics.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TollPlazaResponse {
    private String error;
    private Route route;
    private List<TollPlaza> tollPlazas;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public List<TollPlaza> getTollPlazas() {
        return tollPlazas;
    }

    public void setTollPlazas(List<TollPlaza> tollPlazas) {
        this.tollPlazas = tollPlazas;
    }

    @Override
    public java.lang.String toString() {
        return "TollPlazaResponse{" +
                "error=" + error +
                ", route=" + route +
                ", tollPlazaList=" + tollPlazas +
                '}';
    }
}
