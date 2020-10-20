package io.rxson.rxrest;

import io.reactivex.Flowable;
import io.rxson.reactive.FlowablePath;
import io.rxson.reactive.Streamable;
import io.rxson.reactive.RxPathUtils;
import org.jsfr.json.JsonSurfer;

import java.util.Collection;
import java.util.List;

import static io.rxson.rxrest.SubscriberUtils.bind;

/**
 * @param <T> the subscribed item type
 * <p>
 * A receiver of http messages (stream chunks), and Publisher of chunks as POJOs accordingly
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
final public class MonoReactiveSubscriber<T> extends FlowSubscriber {
    private final Class<T> clazz;
    private final FlowablePath<T> streamInstance;
    private final Streamable<T> streamable;

    public MonoReactiveSubscriber(final Class<T> clazz, final JsonSurfer surfer, final String jsonPath) {
        super(surfer);
        this.clazz = clazz;
        streamable = RxPathUtils.mapMonoFlowablePath(clazz, jsonPath);
        streamInstance = new FlowablePath<>(jsonPath);
        streamInstance.setResult((Flowable<T>) streamable.getPublisher());

        this.nonBlockingParser = surfer.createNonBlockingParser(bind(streamable, clazz, surfer, subscription));
    }

    public FlowablePath<T> getStreamInstance() {
        return streamInstance;
    }

    public Collection<Streamable> getRxPaths() {
        return List.of(streamable);
    }
}
