package com.symverse.symwallet.sdk.exceptions;

public class NotExistReceiverException extends Exception {
    public NotExistReceiverException() {

    }

    public NotExistReceiverException(String message) {
        super(message);
    }

    public NotExistReceiverException(String message, Throwable cause) {
        super(message, cause);
    }
}
