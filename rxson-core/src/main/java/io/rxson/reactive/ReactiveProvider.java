package io.rxson.reactive;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 */
public interface ReactiveProvider {
    default <T> T createInstance(Class<T> clazz) {
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            System.err.println("Failed to create stream instance");
            e.printStackTrace();
            return null;
        }
    }

    <T> Streamable<T> mapMonoFlowablePath(Class<T> clazz, String path);

    List<Streamable> mapFields(Class<?> clazz);
}
