package io.rxson.reactive;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import org.reactivestreams.Publisher;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
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

    Flowable<T> getPublisher();

    FlowableEmitter<T> getFlowEmitter();

    Class<?> getClazz();

}
