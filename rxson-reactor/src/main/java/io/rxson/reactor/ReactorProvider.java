package io.rxson.reactor;

import io.rxson.reactive.FlowablePath;
import io.rxson.reactive.ReactiveIgnore;
import io.rxson.reactive.ReactiveProvider;
import io.rxson.reactive.Streamable;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static io.rxson.reactive.ReflectionUtils.getClassFields;


/**
 * @author Mohamed Aly Bou Hanane
 * © 2020
 */
public final class ReactorProvider implements ReactiveProvider {

    public <T> Streamable<T> mapMonoFlowablePath(final Class<T> clazz, final String path) {
        try {
            return new ReactorPath<>(FlowablePath.class.getField("result"), clazz, path);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Streamable> mapFields(final Class<?> clazz) {
        final Collection<Field> fields = getClassFields(clazz);
        return fields.stream()
            .filter(field -> !field.isAnnotationPresent(ReactiveIgnore.class))
            .map(ReactorPath::new).collect(Collectors.toList());
    }

    public static <T> Streamable<T> mapField(final Field field) {
        return new ReactorPath<>(field);
    }
}
