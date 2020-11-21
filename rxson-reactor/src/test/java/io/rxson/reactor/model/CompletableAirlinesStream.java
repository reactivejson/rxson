package io.rxson.reactor.model;

import io.rxson.reactive.FlatStream;
import io.rxson.rxrest.CompletableStream;
import reactor.core.publisher.Flux;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class CompletableAirlinesStream extends CompletableStream<CompletableAirlinesStream> {
    //jsonPath=$[*]
    @FlatStream
    private Flux<Airline> result;

    public Flux<Airline> getResult() {
        return result;
    }

    public void setResult(final Flux<Airline> result) {
        this.result = result;
    }
}