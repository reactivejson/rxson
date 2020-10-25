package io.rxson.reactive;

import org.reactivestreams.Publisher;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Mohamed Aly Bou Hanane © 2020
 */
public class ReflectionUtils {

    public static boolean isArray(final Field field) {
        return field.getType().isArray() || Collection.class.isAssignableFrom(field.getType());
    }

    public static boolean isPublisher(final Field field) {
        return Publisher.class.isAssignableFrom(field.getType());
    }

    public static Collection<Field> getAllFields(final Class clazz) {

        if (clazz == null) {
            return Collections.emptyList();
        }
        final var result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        var filteredFields = Arrays.stream(clazz.getDeclaredFields())
            .filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers()))
            .collect(Collectors.toList());
        result.addAll(filteredFields);
        return result;
    }

    public static List<Field> getClassFields(final Class clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .collect(Collectors.toList());
    }

    public static List<Field> getClassVisibleFields(final Class clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
            .filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers()))
            .collect(Collectors.toList());
    }

    /**
     * Finds the generic type of the field. If the field is not generic it returns Object.class.
     *
     * @param field to be evaluated
     */
    public static Class<?> getGenericType(final Field field) {
        final Type generic = field.getGenericType();
        if (generic instanceof ParameterizedType) {
            final var actual = ((ParameterizedType) generic).getActualTypeArguments()[0];
            if (actual instanceof Class) {
                return (Class<?>) actual;
            } else if (actual instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) actual).getRawType();
            }
        }
        return Object.class;
    }

    public static Class<?> getType(final Field field) {
        return field.getType();
    }

    public static void invokeSetter(final Object obj, final String fieldName, final Object fieldValue) {
        final PropertyDescriptor pd;
        try {
            pd = new PropertyDescriptor(fieldName, obj.getClass());
            final Method setter = pd.getWriteMethod();
            try {
                setter.invoke(obj, fieldValue);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
    }

    public static String getKey(final Field field) {
        String value = field.getAnnotation(Reactive.class).key();
        return value.isEmpty() ? field.getName() : value;
    }

    public static String getPath(final Field field) {
        String value = field.getAnnotation(Reactive.class).path();
        return value.isEmpty() ? getJsonPath(field) : value;
    }

    public static String getJsonPath(final Field field) {
        return getJsonPath(field, field.getName());
    }

    public static String getJsonPath(final Field field, final String key) {
        String path = "$." + key;
        if (isArray(field) || isPublisher(field))
            path += "[*]";
        else {
            path += ".*";
        }
        return path;
    }

}
