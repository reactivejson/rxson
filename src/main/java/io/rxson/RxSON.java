package io.rxson;

import io.reactivex.Flowable;
import io.rxson.rxrest.CompletableStream;
import io.rxson.rxrest.MonoReactiveSubscriber;
import io.rxson.rxrest.ReactiveSubscriber;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.JsonSurferJackson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

/**
 * @author Mohamed Aly Bou Hanane
 * ©2020
 *
 * <p>
 * An Asynchronous Reactive JSON REST Client to stream any REST resource.
 * {@code RxSON} makes it easy for the application to use JSON streamed chunks
 * from the response as soon as they arrive, and rendering you code fastr.
 * It treats the HTTP response as a series of small, useful chunks and map them to Java Objects
 *
 * <p> An {@code RxSON} can be used to send {@linkplain HttpRequest
 * requests} and retrieve their response as a reactive event-driven, and asynchronous
 * by using observable sequences. It is useful to read lage jason payload without running out og Memory.
 *
 * <p>
 * An {@link HttpClient} is created through a {@code RxSON.Builder().client(HttpClient) builder}.
 * The {@link ReactiveSubscriber} determines how to handle the data chunks received through
 * stream and publish them to subscribers as java objects after mapping .
 *
 * <p><b>Simple Example</b>
 * <pre>{@code
 *         String serviceURL = "https://think.cs.vt.edu/corgis/datasets/json/airlines/airlines.json";
 *         HttpRequest req = HttpRequest.newBuilder(URI.create(serviceURL)).GET().build();
 *         RxSON rxson = new RxSON.Builder().build();
 *
 *         String jsonPath = "$[*].Airport.Name";
 *         Flowable<String> airportStream = rxson.create(String.class, req, jsonPath);
 *         airportStream
 *             .doOnNext(it -> System.out.println("Received new item: " + it))
 *             //Just for test
 *             .toList()
 *             .blockingGet();
 *     }
 *     </pre>
 *
 * <p><b>Detailed Example</b>
 * <b>Create a class Model</b>
 * <pre>{@code public class Airline {
 *     @Reactive(path = "$[*].Airport")
 *     private Flowable<JsonNode> result;
 *     public Flowable<JsonNode> getResult() {
 *         return result;
 *     }
 *     public void setResult(final Flowable<JsonNode> result) {
 *         this.result = result;
 *     }
 * }
 * }
 * </pre>
 *
 * <pre>{@code    RxSON rxrest = new RxSON.Builder().build();
 * HttpRequest req = HttpRequest.newBuilder(URI.create("my.service.com/airlines")).GET().build();
 * var airlinesStream = rxest.create(Airline.class, req);
 *         airlinesStream.getResult()
 *             .observeOn(Schedulers.io())
 *             .subscribeOn(Schedulers.io())
 *             .doOnNext(it -> System.out.println("Received a flow item: " + it.get("Name")))
 *             .subscribe();
 *             //Just for testing
 *             //.toList().blockingGet();
 *       }</pre>
 * @since 11
 */
public final class RxSON {
    private static final String defaultURL = "http://localhost:8080/";

    private final HttpClient client;
    private final JsonSurfer surfer;
    private long n = 1L;

    //TODO, cancel support

    RxSON(final HttpClient client, final JsonSurfer surfer, final long n) {
        this.client = client;
        this.surfer = surfer;
        this.n = n;
    }

    /**
     * Sends the given request asynchronously, create instance of the class and create a reactive stream for class properties
     * It will use default client with GET and http://localhost:8080/
     *
     * @param clazz Target class
     * @param <T>   the response body type
     * @return T an instance of the class
     */
    public <T> T create(final Class<T> clazz) {
        return create(clazz, getDefaultRequest());
    }

    /**
     * Sends the given request asynchronously, create instance of the class and create a reactive stream for class properties
     * <p>
     * Example:
     * {@code Flowable<Airport> airportStream = rxson.create(Airport.class, req, "$[*].Airport.Name");}
     *
     * @param clazz Target class
     * @param req   HttpRequest to send asynchronously
     * @param <T>   the response body type
     * @return T an instance of the class
     */
    public <T> T create(final Class<T> clazz, final HttpRequest req) {
        final var subscriber = new ReactiveSubscriber<>(clazz, surfer);
        subscriber.setN(n);
        final var bodyHandler =
            HttpResponse.BodyHandlers.fromSubscriber(subscriber, ReactiveSubscriber::getStreamInstance);

        client.sendAsync(req, bodyHandler);

        return subscriber.getStreamInstance();
    }

    /**
     * Sends the given request asynchronously, create instance of the class and create a reactive
     * flowable of T
     * <p>
     * Example:
     * {@code Flowable<String> flow = rxson.create(String.class, req, "$[*].Airport.Name");}
     *
     * @param clazz    Target class to create a flowable of
     * @param req      HttpRequest to send asynchronously
     * @param jsonPath The json path to subscribe for
     * @param <T>      A flowable response of clazz
     * @return Flowable a flowable publisher of target type
     */
    public <T> Flowable<T> create(final Class<T> clazz, final HttpRequest req, final String jsonPath) {
        final var subscriber = new MonoReactiveSubscriber<>(clazz, surfer, jsonPath);
        subscriber.setN(n);
        final var bodyHandler =
            HttpResponse.BodyHandlers.fromSubscriber(subscriber, MonoReactiveSubscriber::getStreamInstance);

        client.sendAsync(req, bodyHandler);

        return subscriber.getStreamInstance().getResult();
    }

    /**
     * Send async request, create instance of the class and create a reactive stream for class properties, as well
     * as getting access to the response
     *
     * @param clazz Target class model that extends {@link CompletableStream}
     * @param req   HttpRequest to send asynchronously
     * @param <T>   The generic type of the class
     * @return CompletableStream of target type which allows access to the response as {@link CompletableStream#getAsyncResponse()}
     * <p><b>Example Example</b>
     * {@code getAsyncResponse()
     * .whenComplete((r, t) -> System.out.println("--- Status code " + r.statusCode()))}
     */
    public <T extends CompletableStream<T>> T createCompletable(final Class<T> clazz, final HttpRequest req) {
        final var subscriber = new ReactiveSubscriber<>(clazz, surfer);
        subscriber.setN(n);

        final var bodyHandler =
            HttpResponse.BodyHandlers.fromSubscriber(subscriber, ReactiveSubscriber::getStreamInstance);
        final var asyncResponse = client.sendAsync(req, bodyHandler);

        final var instance = subscriber.getStreamInstance();
        instance.setAsyncResponse(asyncResponse);
        //TODO store the asyncRes to get later
        return instance;
    }

    /**
     * @return An {@link HttpClient} used to send {@link HttpRequest
     * requests} and retrieve their {@link HttpResponse responses}.
     */
    public Optional<HttpClient> getClient() {
        return Optional.ofNullable(client);
    }

    /**
     * Builder for {@code RxSON}
     */
    public static final class Builder {
        private HttpClient client;
        private JsonSurfer surfer = JsonSurferJackson.INSTANCE;
        private long n = 1L;

        public Builder client(final HttpClient client) {
            this.client = client;
            return this;
        }

        /**
         * @param surfer JsonSurfer to be used
         */
        public Builder jsonSurfer(final JsonSurfer surfer) {
            this.surfer = surfer;
            return this;
        }

        /**
         * Adds the given number {@code n} of items to the current
         * unfulfilled demand for this subscription.  If {@code n} is
         * less than or equal to zero, the Subscriber will receive an
         * {@code onError} signal with an {@link
         * IllegalArgumentException} argument.  Otherwise, the
         * Subscriber will receive up to {@code n} additional {@code
         * onNext} invocations (or fewer if terminated).
         *
         * @param n the increment of demand; a value of {@code
         *          Long.MAX_VALUE} may be considered as effectively unbounded
         */
        public Builder n(final long n) {
            this.n = n;
            return this;
        }

        /**
         * Build and return an instance of {@code RxSON}
         *
         * @return RxSON
         */
        public RxSON build() {
            return new RxSON(
                Optional.ofNullable(client).orElse(getDefaultClient()),
                surfer,
                n);
        }

        private HttpClient getDefaultClient() {
            return HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        }
    }

    private HttpRequest getDefaultRequest() {
        return HttpRequest
            .newBuilder(URI.create(defaultURL))
            .GET()
            .build();
    }

}
