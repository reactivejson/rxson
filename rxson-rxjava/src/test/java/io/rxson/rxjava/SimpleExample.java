package io.rxson.rxjava;

import com.fasterxml.jackson.databind.JsonNode;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.rxjava.model.Airport;
import org.junit.Test;

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
 * ...
 */

public class SimpleExample {

    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    /**
     * Example using {@link String}
     */
    @Test
    public void createWithCustomPath_andWithStrings() {
        String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
        String jsonPath = "$[*].Airport.Name";
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();

        RxSON rxson = new RxSON.Builder().provider(new RxProvider()).build();

        Flowable<String> airportStream = (Flowable<String>) rxson.create(String.class, req, jsonPath);
        airportStream
            .doOnNext(it -> System.out.println("Received new item: " + it))
            //Just for test
            .toList()
            .blockingGet();
    }

    /**
     * Example using {@link JsonNode}
     */
    @Test
    public void createWithCustomPath_andWithJsonNode() {
        String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
        HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().build();

        String jsonPath = "$[*].Airport";
        Flowable<JsonNode> airportStream = (Flowable<JsonNode>) rxson.create(JsonNode.class, req, jsonPath);
        airportStream
            .doOnNext(it -> System.out.println("Received new item: " + it.get("Name")))
            //Just for test
            .toList()
            .blockingGet();
    }

    /**
     * Example using java class {@link Airport}
     */
    @Test
    public void createWithCustomPath_andWithClassModel() {
        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().build();

        String jsonPath = "$[*].Airport";
        Flowable<Airport> airportStream = (Flowable<Airport>) rxson.create(Airport.class, req, jsonPath);
        airportStream
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.getName()))
            //Just for test
            .toList()
            .blockingGet();
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

        final RxSON rxson = new RxSON.Builder().client(client).build();

        Flowable<Airport> airportStream = (Flowable<Airport>) rxson.create(Airport.class, req, jsonPath);
        airportStream
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.getName()))
            //Just for test
            .toList()
            .blockingGet();
    }
}