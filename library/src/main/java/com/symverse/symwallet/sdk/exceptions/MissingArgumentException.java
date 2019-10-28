package com.symverse.symwallet.sdk.exceptions;

public class MissingArgumentException extends Exception {
    public MissingArgumentException() {

    }

    public MissingArgumentException(String message) {
        super(message);
    }

    public MissingArgumentException(String message, Throwable cause) {
        super(message, cause);
    }
}
