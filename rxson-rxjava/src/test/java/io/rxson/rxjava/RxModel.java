package io.rxson.rxjava;

import io.reactivex.Flowable;
import io.rxson.reactive.FlatStream;
import io.rxson.reactive.Reactive;
import io.rxson.reactive.ReactiveIgnore;
import io.rxson.rxjava.model.Airport;

import java.util.Map;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */

public class RxModel {

    Flowable<Airport> airports;

    @FlatStream
    Flowable<String> flat;

    @Reactive(path = "$.airlines[*]")
    Flowable<Long> airlines;

    @Reactive(path = "$.map[*]")
    Flowable<Map<String, Airport>> map;

    @ReactiveIgnore
    String ignoreMe;

    public RxModel() {

    }

    public Flowable<Airport> getAirports() {
        return airports;
    }

    public void setAirports(Flowable<Airport> airports) {
        this.airports = airports;
    }

    public Flowable<String> getFlat() {
        return flat;
    }

    public void setFlat(Flowable<String> flat) {
        this.flat = flat;
    }

    public Flowable<Long> getAirlines() {
        return airlines;
    }

    public void setAirlines(Flowable<Long> airlines) {
        this.airlines = airlines;
    }

    public Flowable<Map<String, Airport>> getMap() {
        return map;
    }

    public void setMap(Flowable<Map<String, Airport>> map) {
        this.map = map;
    }
}
