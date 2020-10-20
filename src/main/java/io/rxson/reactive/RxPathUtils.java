package io.rxson.reactive;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.rxson.reactive.ReflectionUtils.getClassFields;


/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public final class RxPathUtils {

    public static <T> T createInstance(final Class<T> clazz) {
        try {
            final Constructor<T> constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            System.err.println("Failed to create stream instance");
            e.printStackTrace();
            return null;
        }
    }

    public static <T> Streamable<T> mapMonoFlowablePath(final Class<T> clazz, final String path) {
        try {
            return new RxPath<T>(FlowablePath.class.getField("result"), clazz, path);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Streamable> mapFields(final Class<?> clazz) {
        final Collection<Field> fields = getClassFields(clazz);
        return fields.stream()
            .filter(field -> !field.isAnnotationPresent(ReactiveIgnore.class))
            .map((Function<Field, RxPath>) RxPath::new).collect(Collectors.toList());
    }
}
