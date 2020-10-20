package io.rxson.rxrest;

import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.examples.model.AirlinesStream;
import io.rxson.examples.model.CompletableAirlinesStream;
import io.rxson.examples.model.JsonNodeModel;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class ReactiveSubscriberTest {

    private static final HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    private static final String airlinesServiceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";

    @Test
    public void createFlatStream() {
        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest
            .newBuilder(URI.create(airlinesServiceURL))
            .GET()
            .build();
        RxSON rxson = new RxSON.Builder()
            .client(client)
            .build();

        final var airlinesStream = rxson.create(AirlinesStream.class, req);
        airlinesStream.getResult()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.airport.getName());
            })
            .toList().blockingGet();
        Assert.assertTrue(count.get() > 0);
    }

    @Test
    public void createForReactivePath() {
        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest.newBuilder(URI.create(airlinesServiceURL)).GET().build();
        final RxSON rxest = new RxSON.Builder().build();
        final var airlinesStream = rxest.create(JsonNodeModel.class, req);
        airlinesStream.getResult()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.get("Name"));
            })
            .toList().blockingGet();
        Assert.assertTrue(count.get() > 0);
    }

    @Test
    public void testCreateCompletable_ShouldRetunCompletable() throws ExecutionException, InterruptedException {
        final var count = new AtomicInteger();
        final HttpRequest airlinesReq = HttpRequest.newBuilder(URI.create(airlinesServiceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().client(client).build();

        CompletableAirlinesStream airlinesStream = rxson.createCompletable(CompletableAirlinesStream.class, airlinesReq);
        airlinesStream.getResult()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received airlines item: " + it.getAirport().getName());
            })
            .doOnComplete(() -> System.out.println("airlinesStream done"))
            .subscribe();

        final var res =
            airlinesStream.getAsyncResponse()
                .whenComplete((r, t) -> {
                    Assert.assertEquals(200, r.statusCode());
                })
                .thenApply(HttpResponse::body);
        res.get();
        System.out.println("Count: " + count.get());
        Assert.assertTrue(count.get() > 0);

    }
}
