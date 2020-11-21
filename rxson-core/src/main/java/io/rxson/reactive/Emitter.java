package io.rxson.reactive;

import org.reactivestreams.Subscriber;

/**
 * @param <T> the value type
 * @author Mohamed Aly Bou Hanane
 * <p>
 * Wrapper API around a json stream Subscriber for emitting received stream chunks
 * followed by zero or one onError/onComplete.
 *
 */
public interface Emitter<T> {
    /**
     * Emit new value.
     *
     * @param value the value to emit
     * @see Subscriber#onNext(Object)
     */
    void next(T value);

    /**
     * Emit a Throwable exception.
     *
     * @param error the exception to emit
     */
    void error(Throwable error);

    /**
     * Send successful completion event.
     * <p>
     * No further events will be sent.
     */
    void complete();

    /**
     * Returns true if the downstream cancelled the sequence.
     *
     * @return true if the downstream cancelled the sequence
     */
    boolean isCancelled();
}