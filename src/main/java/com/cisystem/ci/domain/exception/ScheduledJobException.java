package com.cisystem.ci.domain.exception;

public class ScheduledJobException extends RuntimeException {
    public ScheduledJobException(String message, Exception exception) {
        super(message, exception);
    }
}
