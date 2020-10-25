package io.rxson.reactor.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Airport {
    @JsonProperty("Code")
    public String code;
    @JsonProperty("Name")
    public String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
