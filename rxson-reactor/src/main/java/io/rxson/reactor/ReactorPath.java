package io.rxson.reactor;

import io.rxson.reactive.Emitter;
import io.rxson.reactive.ReactivePath;
import io.rxson.reactive.ReflectionUtils;
import io.rxson.rxrest.UnsupportedReactiveType;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 *
 * <p>
 * Wrapper API around a json field that create a {@link Flux} of class fielad
 * <p>
 * Programmatically create a {@link Flux} with the capability of emitting multiple
 * json stream chunks in asynchronous manner through the {@link Emitter} API.
 */
final class ReactorPath<T> extends ReactivePath<T> {

    private Flux<T> flowable;

    public ReactorPath(final Field field, final Class<?> clazz, final String path) {
        this.field = field;
        this.clazz = clazz;
        this.jsonPath = path;
        setupPublisher();
    }

    public ReactorPath(final Field field) {
        this(field, ReflectionUtils.getGenericType(field), getJsonPath(field));
    }

    private void setupPublisher() {
        if (isPublisher()) {
            flowable =
                Flux
                    .create(emitter -> this.emitter = new ReactorEmitter<T>(emitter), FluxSink.OverflowStrategy.BUFFER);
        } else {
            throw new UnsupportedReactiveType("The field type is not supported");
        }
    }

    @Override
    public Flux<T> getPublisher() {
        return flowable;
    }

}
