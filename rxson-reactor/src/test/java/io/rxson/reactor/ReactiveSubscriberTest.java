package io.rxson.reactor;

import io.rxson.RxSON;
import io.rxson.reactor.model.AirlinesStream;
import io.rxson.reactor.model.CompletableAirlinesStream;
import io.rxson.reactor.model.JsonNodeModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import reactor.core.scheduler.Schedulers;

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
    private static RxSON rxson;
    private static HttpRequest req;

    @BeforeClass
    public static void setUp() {
        rxson = new RxSON.Builder()
            .provider(new ReactorProvider())
            .client(client)
            .build();

        req = HttpRequest
            .newBuilder(URI.create(airlinesServiceURL))
            .GET()
            .build();
    }

    @Test
    public void createFlatStream() {
        final var count = new AtomicInteger();

        final var airlinesStream = rxson.create(AirlinesStream.class, req);
        airlinesStream.getResult()
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.airport.getName());
            }).blockLast();
        Assert.assertTrue(count.get() > 0);
    }

    @Test
    public void createForReactivePath() {
        final var count = new AtomicInteger();

        final var airlinesStream = rxson.create(JsonNodeModel.class, req);
        airlinesStream.getResult()
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
            .doOnNext(it -> {
                count.getAndIncrement();
                System.out.println("Received a flow item: " + it.get("Name"));
            })
            .blockLast();
        Assert.assertTrue(count.get() > 0);
    }

    @Test
    public void testCreateCompletable_ShouldRetunCompletable() throws ExecutionException, InterruptedException {
        final var count = new AtomicInteger();

        CompletableAirlinesStream airlinesStream = rxson.createCompletable(CompletableAirlinesStream.class, req);
        airlinesStream.getResult()
            .publishOn(Schedulers.parallel())
            .subscribeOn(Schedulers.parallel())
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