package io.rxson.examples.model.airline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airline {
    @JsonProperty("Airport")
    public Airport airport;
    @JsonProperty("Time")
    public Time time;
    @JsonProperty("Statistics")
    public Statistics statistics;

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
