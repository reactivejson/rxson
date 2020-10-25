package io.rxson.rxjava.model;

import io.reactivex.Flowable;
import io.rxson.reactive.FlatStream;
import io.rxson.rxrest.CompletableStream;

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