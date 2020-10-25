package io.rxson.reactor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistics {
    @JsonProperty("# of Delays")
    public OfDelays ofDelays;
    @JsonProperty("Carriers")
    public Carriers carriers;
    @JsonProperty("Flights")
    public Flights flights;
    @JsonProperty("Minutes Delayed")
    public MinutesDelayed minutesDelayed;

    public OfDelays getOfDelays() {
        return ofDelays;
    }

    public void setOfDelays(OfDelays ofDelays) {
        this.ofDelays = ofDelays;
    }

    public Carriers getCarriers() {
        return carriers;
    }

    public void setCarriers(Carriers carriers) {
        this.carriers = carriers;
    }

    public Flights getFlights() {
        return flights;
    }

    public void setFlights(Flights flights) {
        this.flights = flights;
    }

    public MinutesDelayed getMinutesDelayed() {
        return minutesDelayed;
    }

    public void setMinutesDelayed(MinutesDelayed minutesDelayed) {
        this.minutesDelayed = minutesDelayed;
    }
}
