package io.rxson.reactor;

import io.rxson.reactive.Emitter;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class ReactorEmitterTest {
    static Emitter<String> emitter;
    static Flux<String> flux;
    private static final String next1 = "next1";
    private static final String next2 = "next2";

    @BeforeClass
    public static void setUp() {
        flux = Flux.<String>create(sink -> emitter = new ReactorEmitter<>(sink), FluxSink.OverflowStrategy.BUFFER);
    }

    @Test
    public void onNext() throws InterruptedException {
        new Thread(() -> flux.doOnNext(it -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            MatcherAssert.assertThat(it, CoreMatchers.anyOf(CoreMatchers.is(next1), CoreMatchers.is(next2)));
        }).subscribe()).start();
        Thread.sleep(200);
        emitter.next(next1);
        emitter.next(next2);
        Assert.assertNotNull(emitter);
        emitter.complete();
    }
}