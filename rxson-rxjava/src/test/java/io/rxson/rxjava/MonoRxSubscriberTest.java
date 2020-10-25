package io.rxson.rxjava;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.rxjava.model.Airport;
import org.junit.Test;
import org.reactivestreams.Publisher;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.Assert.assertTrue;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class MonoRxSubscriberTest {

    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    @Test
    public void createSimpleWithPath() {
        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().provider(new RxProvider()).build();

        String jsonPath = "$[*].Airport";
        Publisher<Airport> airportStream = rxson.create(Airport.class, req, jsonPath);
        assertTrue(airportStream instanceof Flowable);
        Flowable<Airport> airportFlowable = (Flowable<Airport>) airportStream;
        airportFlowable
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.getName());
            })
            //Just for test
            .toList()
            .blockingGet();
        assertTrue(count.get() > 0);
    }
}
