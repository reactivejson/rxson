package io.rxson.reactive;

import org.reactivestreams.Publisher;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 *
 * <p>
 * Wrapper API around a json field that create a {@link Publisher} of class fielad
 * <p>
 * Programmatically create a {@link Publisher} with the capability of emitting multiple
 * json stream chunks in asynchronous manner through the {@link Emitter} API.
 */
public interface Streamable<T> {
    default void invokeSetter(final Object obj) {
        ReflectionUtils.invokeSetter(obj, getField().getName(), getPublisher());
    }

    default Class<?> getGenericType() {
        return ReflectionUtils.getGenericType(getField());
    }

    default boolean isPublisher() {
        return Publisher.class.isAssignableFrom(getField().getType());
    }

    Field getField();

    String getJsonPath();

    Publisher<T> getPublisher();

    Emitter<T> getFlowEmitter();

    Class<?> getClazz();
}
