package io.rxson.examples;

import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.examples.model.AirlinesStream;
import io.rxson.examples.model.JsonNodeModel;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxSONExample {

    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    @Test
    public void create() {
        final HttpRequest req = HttpRequest
            .newBuilder(URI.create(serviceURL))
            .GET()
            .build();
        RxSON rxson = new RxSON.Builder()
            .client(client)
            .build();

        final var airlinesStream = rxson.create(AirlinesStream.class, req);
        airlinesStream.getResult()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.airport.getName()))
            .toList().blockingGet();
    }

    @Test
    public void createForDynamic() {
        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        final RxSON rxest = new RxSON.Builder().build();
        final var airlinesStream = rxest.create(JsonNodeModel.class, req);
        airlinesStream.getResult()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> System.out.println("Received a flow item: " + it.get("Name")))
            .toList().blockingGet();
    }
}