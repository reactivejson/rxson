package io.rxson.reactor;

import com.fasterxml.jackson.databind.JsonNode;
import io.rxson.RxSON;
import io.rxson.reactor.model.Airport;
import org.junit.Assert;
import org.junit.Test;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 * <p>
 * Given Json:
 * [
 * {
 * "Airport": {
 * "Code": "LA",
 * "Name": "LA: LA International"
 * },
 * //Other objects
 * },
 * {
 * "Airport": {
 * "Code": "ATL",
 * "Name": "Atlanta: Atlanta International"
 * }
 * }
 * ]
 */

public class SimpleExample {

    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    /**
     * Example using {@link String}
     */
    @Test
    public void createWithCustomPath_andWithStrings() {
        String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().provider(new ReactorProvider()).build();

        String jsonPath = "$[*].Airport.Name";

        Publisher<String> airportStream = rxson.create(String.class, req, jsonPath);
        Assert.assertTrue(airportStream instanceof Flux);
        Flux<String> airportFlowable = (Flux<String>) airportStream;

        airportFlowable
            .doOnNext(it -> System.out.println("Received new item: " + it))
            //Just for test
            .blockLast();
    }

    /**
     * Example using {@link JsonNode}
     */
    @Test
    public void createWithCustomPath_andWithJsonNode() {
        String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().provider(new ReactorProvider()).build();

        String jsonPath = "$[*].Airport";
        Flux<JsonNode> airportStream = (Flux<JsonNode>) rxson.create(JsonNode.class, req, jsonPath);
        airportStream
            .doOnNext(it -> System.out.println("Received new item: " + it.get("Name")))
            //Just for test
            .blockLast();
    }

    /**
     * Example using java class {@link Airport}
     */
    @Test
    public void createWithCustomPath_andWithClassModel() {
        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().provider(new ReactorProvider()).build();

        String jsonPath = "$[*].Airport";
        Flux<Airport> airportStream = (Flux<Airport>) rxson.create(Airport.class, req, jsonPath);
        airportStream
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.getName()))
            //Just for test
            .blockLast();
    }

    /**
     * Example using java class {@link Airport}
     */
    @Test
    public void customExample() {
        final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
        final String jsonPath = "$[*].Airport";

        final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();

        final RxSON rxson = new RxSON.Builder().client(client).provider(new ReactorProvider()).build();

        Flux<Airport> airportStream = (Flux<Airport>) rxson.create(Airport.class, req, jsonPath);
        airportStream
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.getName()))
            //Just for test
            .blockLast();
    }
}