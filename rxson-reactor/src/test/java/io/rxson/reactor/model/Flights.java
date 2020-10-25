package io.rxson.reactor.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Flights {
    @JsonProperty("Cancelled")
    public int cancelled;
    @JsonProperty("Delayed")
    public int delayed;
    @JsonProperty("Diverted")
    public int diverted;
    @JsonProperty("On Time")
    public int onTime;
    @JsonProperty("Total")
    public int total;

    public int getCancelled() {
        return cancelled;
    }

    public void setCancelled(int cancelled) {
        this.cancelled = cancelled;
    }

    public int getDelayed() {
        return delayed;
    }

    public void setDelayed(int delayed) {
        this.delayed = delayed;
    }

    public int getDiverted() {
        return diverted;
    }

    public void setDiverted(int diverted) {
        this.diverted = diverted;
    }

    public int getOnTime() {
        return onTime;
    }

    public void setOnTime(int onTime) {
        this.onTime = onTime;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
