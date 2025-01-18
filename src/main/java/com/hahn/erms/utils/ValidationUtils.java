package com.hahn.erms.utils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    public static <T> void validate(T object) {
        if (object == null) {
            throw new IllegalArgumentException("Object to validate cannot be null");
        }

        Set<ConstraintViolation<T>> violations = validator.validate(object);

        if (!violations.isEmpty()) {
            String errorMessage = violations.stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Validation failed: " + errorMessage);
        }
    }


    public static void validateAll(Object... objects) {
        StringBuilder errorMessages = new StringBuilder();

        for (Object object : objects) {
            if (object != null) {
                Set<ConstraintViolation<Object>> violations = validator.validate(object);
                for (ConstraintViolation<Object> violation : violations) {
                    errorMessages.append(violation.getPropertyPath())
                            .append(": ")
                            .append(violation.getMessage())
                            .append("\n");
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            throw new IllegalArgumentException("Validation failed:\n" + errorMessages.toString());
        }
    }
}
