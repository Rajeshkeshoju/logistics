package com.example.logstics.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TollPlazaResponse {
    private String error;
    private Route route;
    private List<TollPlaza> tollPlazaList;

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

    public List<TollPlaza> getTollPlazaList() {
        return tollPlazaList;
    }

    public void setTollPlazaList(List<TollPlaza> tollPlazaList) {
        this.tollPlazaList = tollPlazaList;
    }

    @Override
    public java.lang.String toString() {
        return "TollPlazaResponse{" +
                "error=" + error +
                ", route=" + route +
                ", tollPlazaList=" + tollPlazaList +
                '}';
    }
}
