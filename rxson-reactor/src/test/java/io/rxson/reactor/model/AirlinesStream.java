package io.rxson.reactor.model;

import io.rxson.reactive.FlatStream;
import io.rxson.reactive.ReactiveIgnore;
import reactor.core.publisher.Flux;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class AirlinesStream {
    //jsonPath=$[*]
    @FlatStream
    private Flux<Airline> result;

    @ReactiveIgnore
    String ignoreMe;

    public Flux<Airline> getResult() {
        return result;
    }

    public void setResult(final Flux<Airline> result) {
        this.result = result;
    }
}