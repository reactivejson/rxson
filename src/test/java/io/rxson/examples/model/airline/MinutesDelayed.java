package io.rxson.examples.model.airline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MinutesDelayed {
    @JsonProperty("Carrier")
    public int carrier;
    @JsonProperty("Late Aircraft")
    public int lateAircraft;
    @JsonProperty("National Aviation System")
    public int nationalAviationSystem;
    @JsonProperty("Security")
    public int security;
    @JsonProperty("Total")
    public int total;
    @JsonProperty("Weather")
    public int weather;

    public int getCarrier() {
        return carrier;
    }

    public void setCarrier(int carrier) {
        this.carrier = carrier;
    }

    public int getLateAircraft() {
        return lateAircraft;
    }

    public void setLateAircraft(int lateAircraft) {
        this.lateAircraft = lateAircraft;
    }

    public int getNationalAviationSystem() {
        return nationalAviationSystem;
    }

    public void setNationalAviationSystem(int nationalAviationSystem) {
        this.nationalAviationSystem = nationalAviationSystem;
    }

    public int getSecurity() {
        return security;
    }

    public void setSecurity(int security) {
        this.security = security;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWeather() {
        return weather;
    }

    public void setWeather(int weather) {
        this.weather = weather;
    }
}
