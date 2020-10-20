package io.rxson.examples.model;

import io.reactivex.Flowable;
import io.rxson.rxrest.CompletableStream;
import io.rxson.reactive.FlatStream;
import io.rxson.examples.model.airline.Airline;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class CompletableAirlinesStream extends CompletableStream<CompletableAirlinesStream> {
    //jsonPath=$[*]
    @FlatStream
    private Flowable<Airline> result;

    public Flowable<Airline> getResult() {
        return result;
    }

    public void setResult(final Flowable<Airline> result) {
        this.result = result;
    }
}