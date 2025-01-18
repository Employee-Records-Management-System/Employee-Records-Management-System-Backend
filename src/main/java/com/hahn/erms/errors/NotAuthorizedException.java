package com.hahn.erms.errors;

import java.io.Serial;

public class NotAuthorizedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1;

    public NotAuthorizedException(String message) {
        super(message);
    }
}
