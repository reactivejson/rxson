package io.rxson.examples.model.airline;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Time {
    @JsonProperty("Label")
    public String label;
    @JsonProperty("Month")
    public int month;
    @JsonProperty("Month Name")
    public String monthName;
    @JsonProperty("Year")
    public int year;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
