package io.rxson.reactive;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.rxson.rxrest.UnsupportedReactiveType;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
class RxPath<T> implements Streamable<T> {

    protected final Field field;
    protected final Class<?> clazz;
    protected final String jsonPath;
    protected FlowableEmitter<T> flowableEmitter;
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
                    .create(emitter -> this.flowableEmitter = emitter, BackpressureStrategy.BUFFER);
        } else {
            throw new UnsupportedReactiveType("The field type is not supported");
        }
    }

    @Override
    public Flowable<T> getPublisher() {
        return flowable;
    }

    @Override
    public FlowableEmitter<T> getFlowEmitter() {
        return flowableEmitter;
    }

    public String getJsonPath() {
        return jsonPath;
    }

    @Override
    public Field getField() {
        return field;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public static String getJsonPath(final Field field) {
        field.setAccessible(true);
        if (field.isAnnotationPresent(Reactive.class)) {
            String path = field.getAnnotation(Reactive.class).path();
            return path.isEmpty() ? ReflectionUtils.getJsonPath(field, ReflectionUtils.getKey(field)) : path;
        }
        if (field.isAnnotationPresent(FlatStream.class)) {
            return "$[*]";
        }
        return ReflectionUtils.getJsonPath(field);
    }
}
