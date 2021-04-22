package com.github.saleco.interview.calendar.api.exception;


import java.util.UUID;

public abstract class BusinessException extends RuntimeException implements InterviewCalendarAPIException {

    private final String errorSerial = UUID.randomUUID().toString();
    private final String errorCode;

    protected BusinessException(String errorCode) {
        this.errorCode = errorCode;
    }

    protected BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected BusinessException(String errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public final String getErrorSerial() {
        return this.errorSerial;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public final Throwable asThrowable() {
        return this;
    }
}

