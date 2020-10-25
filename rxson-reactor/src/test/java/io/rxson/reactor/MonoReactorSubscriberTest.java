package io.rxson.reactor;

import io.rxson.RxSON;
import io.rxson.reactor.model.Airport;
import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class MonoReactorSubscriberTest {

    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    @Test
    public void createSimpleWithPath() {
        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().provider(new ReactorProvider()).build();

        String jsonPath = "$[*].Airport";
        Publisher<Airport> airportStream = rxson.create(Airport.class, req, jsonPath);
        Assert.assertTrue(airportStream instanceof Flux);
        Flux<Airport> airportFlux = (Flux<Airport>) airportStream;
        airportFlux
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.getName());
            })
            //Just for test
            .blockLast();
        Assert.assertTrue(count.get() > 0);
    }
}
