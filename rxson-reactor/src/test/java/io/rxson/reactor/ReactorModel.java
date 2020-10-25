package io.rxson.reactor;

import io.rxson.reactive.FlatStream;
import io.rxson.reactive.Reactive;
import io.rxson.reactive.ReactiveIgnore;
import io.rxson.reactor.model.Airport;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */

public class ReactorModel {

    Flux<Airport> airports;

    @FlatStream
    Flux<String> flat;

    @Reactive(path = "$.airlines[*]")
    Flux<Long> airlines;

    @Reactive(path = "$.map[*]")
    Flux<Map<String, Airport>> map;

    @ReactiveIgnore
    String ignoreMe;

    public ReactorModel() {

    }

    public Flux<Airport> getAirports() {
        return airports;
    }

    public void setAirports(Flux<Airport> airports) {
        this.airports = airports;
    }

    public Flux<String> getFlat() {
        return flat;
    }

    public void setFlat(Flux<String> flat) {
        this.flat = flat;
    }

    public Flux<Long> getAirlines() {
        return airlines;
    }

    public void setAirlines(Flux<Long> airlines) {
        this.airlines = airlines;
    }

    public Flux<Map<String, Airport>> getMap() {
        return map;
    }

    public void setMap(Flux<Map<String, Airport>> map) {
        this.map = map;
    }
}
