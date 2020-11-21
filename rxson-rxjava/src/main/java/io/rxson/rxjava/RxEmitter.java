package io.rxson.rxjava;

import io.reactivex.FlowableEmitter;
import io.rxson.reactive.Emitter;

/**
 * @param <T> the value type
 * @author Mohamed Aly Bou Hanane
 * <p>
 * Wrapper API around {@link FlowableEmitter} for emitting received stream chunks
 * followed by zero or one onError/onComplete.
 * <p>
 */
final class RxEmitter<T> implements Emitter<T> {
    private final FlowableEmitter<T> emitter;

    public RxEmitter(final FlowableEmitter<T> emitter) {
        this.emitter = emitter;
    }

    /**
     * @param val the value to emit
     * @see FlowableEmitter#onNext(T val)
     */
    @Override
    public void next(final T val) {
        emitter.onNext(val);
    }

    /**
     * @param throwable the exception to signal
     * @see FlowableEmitter#onError(Throwable t)
     */
    @Override
    public void error(final Throwable throwable) {
        emitter.onError(throwable);
    }

    /**
     * @see FlowableEmitter#onComplete()
     */
    @Override
    public void complete() {
        emitter.onComplete();
    }

    public boolean isCancelled() {
        return emitter.isCancelled();
    }
}
