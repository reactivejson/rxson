package io.rxson.reactive;

import java.lang.reflect.Field;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 */
public abstract class ReactivePath<T> implements Streamable<T> {
    protected Field field;
    protected Class<?> clazz;
    protected String jsonPath;
    protected Emitter<T> emitter;

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

    @Override
    public Emitter<T> getFlowEmitter() {
        return emitter;
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
}
