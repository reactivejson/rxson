package io.rxson.examples.model.airline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Carriers {
    @JsonProperty("Names")
    public String names;
    @JsonProperty("Total")
    public int total;

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
