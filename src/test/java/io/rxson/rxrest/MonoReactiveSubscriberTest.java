package io.rxson.rxrest;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.examples.model.airline.Airport;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class MonoReactiveSubscriberTest {

    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    @Test
    public void createSimpleWithPath() {
        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().build();

        String jsonPath = "$[*].Airport";
        Flowable<Airport> airportStream = rxson.create(Airport.class, req, jsonPath);
        airportStream
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.getName());
            })
            //Just for test
            .toList()
            .blockingGet();
        Assert.assertTrue(count.get() > 0);
    }
}
