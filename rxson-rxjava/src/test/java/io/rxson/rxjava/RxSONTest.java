package io.rxson.rxjava;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import io.rxson.RxSON;
import io.rxson.rxjava.model.Airport;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxSONTest {
    private static final String serviceURL = "http://localhost:1080/stream";
    String payload = "[\n" +
        "  {\n" +
        "    \"Airport\": {\n" +
        "      \"Code\": \"LA\",\n" +
        "      \"Name\": \"LA: LA International\"\n" +
        "    }\n" +
        "  },\n" +
        "  {\n" +
        "    \"Airport\": {\n" +
        "      \"Code\": \"ATL\",\n" +
        "      \"Name\": \"Atlanta: Atlanta International\"\n" +
        "    }\n" +
        "  }\n" +
        "]";

    private static ClientAndServer mockServer;

    @BeforeClass
    public static void startServer() {
        mockServer = startClientAndServer(1080);
    }

    @AfterClass
    public static void stopServer() {
        mockServer.stop();
    }

    @Test
    public void test() {
        new MockServerClient("localhost", 1080)
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/stream")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withBody(payload)
            );

        final var count = new AtomicInteger();

        final HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
        RxSON rxson = new RxSON.Builder().build();

        String jsonPath = "$[*].Airport";
        Flowable<Airport> airportStream = (Flowable<Airport>) rxson.create(Airport.class, req, jsonPath);
        airportStream
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .doOnNext(it -> {
                count.getAndIncrement();
                assertThat(it.getCode(), anyOf(is("LA"), is("ATL")));
                System.out.println("Received a flow item: " + it.getName());
            })
            //Just for test
            .toList()
            .blockingGet();
        Assert.assertTrue(count.get() > 0);
    }
}
