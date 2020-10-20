package io.rxson.rxrest;

import io.rxson.reactive.Streamable;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.NonBlockingParser;

import java.nio.ByteBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Flow;

import static io.rxson.rxrest.SubscriberUtils.toBytes;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 * <p>
 * A receiver of http messages (stream chunks), and Publisher of chunks as POJOs accordingly
 */
public abstract class FlowSubscriber implements Flow.Subscriber<List<ByteBuffer>> {
    protected Flow.Subscription subscription;
    protected final JsonSurfer surfer;
    protected NonBlockingParser nonBlockingParser;
    protected long n = 1L;

    public FlowSubscriber(final JsonSurfer surfer) {
        this.surfer = surfer;
    }

    @Override
    public void onSubscribe(final Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(n); // Request first item
    }

    @Override
    public void onNext(final List<ByteBuffer> buffers) {
        final var chunk = toBytes(buffers);
        nonBlockingParser.feed(chunk, 0, chunk.length);
        subscription.request(n); // Request next item
    }

    @Override
    public void onError(final Throwable throwable) {
        this.getRxPaths()
            .forEach(jsonStreamField -> {
                final var emitter = jsonStreamField.getFlowEmitter();
                emitter.onError(throwable);
            });
    }

    @Override
    public void onComplete() {
        this.getRxPaths()
            .forEach(jsonStreamField -> {
                final var emitter = jsonStreamField.getFlowEmitter();
                if (!emitter.isCancelled()) {
                    emitter.onComplete();
                }

            });
    }

    public long getN() {
        return n;
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
    public void setN(final long n) {
        this.n = n;
    }

    abstract Collection<Streamable> getRxPaths();

}
