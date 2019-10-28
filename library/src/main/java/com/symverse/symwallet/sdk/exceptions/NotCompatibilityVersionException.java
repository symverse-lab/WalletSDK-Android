package com.symverse.symwallet.sdk.exceptions;

public class NotCompatibilityVersionException extends Exception {
    private long cerrentVersion;
    private long minimumVersion;

    public NotCompatibilityVersionException(String message, long cerrentVersion, long minimumVersion) {
        super(message);
        this.cerrentVersion = cerrentVersion;
        this.minimumVersion = minimumVersion;
    }

    public NotCompatibilityVersionException(String message, Throwable cause, long cerrentVersion, long minimumVersion) {
        super(message, cause);
        this.cerrentVersion = cerrentVersion;
        this.minimumVersion = minimumVersion;
    }

    public long getCerrentVersion() {
        return cerrentVersion;
    }

    public long getMinimumVersion() {
        return minimumVersion;
    }
}
