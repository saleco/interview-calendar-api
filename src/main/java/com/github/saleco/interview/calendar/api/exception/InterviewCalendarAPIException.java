package com.github.saleco.interview.calendar.api.exception;


public interface InterviewCalendarAPIException {
    String getErrorCode();

    String getErrorSerial();

    default String getSupportReference() {
        return this.getErrorCode() + "-" + this.getErrorSerial();
    }

    String getMessage();

}
