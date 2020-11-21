package io.rxson.reactor;

import io.rxson.reactive.Emitter;
import reactor.core.publisher.FluxSink;

/**
 * @param <T> the value type
 * @author Mohamed Aly Bou Hanane
 * <p>
 * Wrapper API around {@link FluxSink} for emitting received stream chunks
 * followed by zero or one onError/onComplete.
 * <p>
 */
final class ReactorEmitter<T> implements Emitter<T> {
    private final FluxSink<T> emitter;

    public ReactorEmitter(final FluxSink<T> emitter) {
        this.emitter = emitter;
    }

    /**
     * @param val the value to emit
     * @see FluxSink#next(T val)
     */
    @Override
    public void next(final T val) {
        emitter.next(val);
    }

    /**
     * @param throwable the exception to signal
     * @see FluxSink#error(Throwable t)
     */
    @Override
    public void error(final Throwable throwable) {
        emitter.error(throwable);
    }

    /**
     * @see FluxSink#complete()
     */
    @Override
    public void complete() {
        emitter.complete();
    }

    public boolean isCancelled(){
        return emitter.isCancelled();
    }
}
