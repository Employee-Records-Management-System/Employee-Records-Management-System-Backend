package com.hahn.erms.utils;

import java.lang.reflect.Field;

public class EntityUtils {

    public static void copyNonNullProperties(Object source, Object target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target must not be null");
        }

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();

        if (!sourceClass.equals(targetClass)) {
            throw new IllegalArgumentException("Source and target must be of the same class");
        }

        try {
            for (Field field : sourceClass.getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(source);

                if (value != null) {
                    if (isSimpleType(value.getClass())) {
                        field.set(target, value);
                    } else {
                        Field targetField = targetClass.getDeclaredField(field.getName());
                        targetField.setAccessible(true);
                        Object targetValue = targetField.get(target);

                        if (targetValue == null) {
                            targetValue = value.getClass().getDeclaredConstructor().newInstance();
                            targetField.set(target, targetValue);
                        }

                        copyNonNullProperties(value, targetValue);
                    }
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error copying properties", e);
        }
    }

    private static boolean isSimpleType(Class<?> type) {
        return type.isPrimitive() ||
                type.equals(String.class) ||
                type.equals(Boolean.class) ||
                type.equals(Character.class) ||
                type.equals(Byte.class) ||
                type.equals(Short.class) ||
                type.equals(Integer.class) ||
                type.equals(Long.class) ||
                type.equals(Float.class) ||
                type.equals(Double.class) ||
                type.equals(java.time.LocalDate.class) ||
                type.isEnum();
    }
}