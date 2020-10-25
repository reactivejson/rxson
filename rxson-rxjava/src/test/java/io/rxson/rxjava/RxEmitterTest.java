package io.rxson.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.rxson.reactive.Emitter;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public class RxEmitterTest {
    static Emitter<String> emitter;
    static Flowable<String> flux;
    private static final String next1 = "next1";
    private static final String next2 = "next2";

    @BeforeClass
    public static void setUp() {
        flux = Flowable.create(sink -> emitter = new RxEmitter<>(sink), BackpressureStrategy.BUFFER);
    }

    @Test
    public void onNext() throws InterruptedException {
        new Thread(() -> flux.doOnNext(it -> {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertThat(it, anyOf(is(next1), is(next2)));
        }).subscribe()).start();
        Thread.sleep(200);
        emitter.next(next1);
        emitter.next(next2);
        assertNotNull(emitter);
        emitter.complete();
    }
}