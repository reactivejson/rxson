package io.rxson.rxjava;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.rxson.reactive.Emitter;
import io.rxson.reactive.ReactivePath;
import io.rxson.reactive.ReflectionUtils;
import io.rxson.rxrest.UnsupportedReactiveType;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 *
 * <p>
 * Wrapper API around a json field that create a {@link Flowable} of class fielad
 * <p>
 * Programmatically create a {@link Flowable} with the capability of emitting multiple
 * json stream chunks in asynchronous manner through the {@link Emitter} API.
 */
final class RxPath<T> extends ReactivePath<T> {

    protected Flowable<T> flowable;

    public RxPath(final Field field, final Class<?> clazz, final String path) {
        this.field = field;
        this.clazz = clazz;
        this.jsonPath = path;
        setupPublisher();
    }

    public RxPath(final Field field) {
        this(field, ReflectionUtils.getGenericType(field), getJsonPath(field));
    }

    private void setupPublisher() {
        if (isPublisher()) {
            flowable =
                Flowable
                    .create(emitter -> this.emitter = new RxEmitter<T>(emitter), BackpressureStrategy.BUFFER);
        } else {
            throw new UnsupportedReactiveType("The field type is not supported");
        }
    }

    @Override
    public Flowable<T> getPublisher() {
        return flowable;
    }
}
