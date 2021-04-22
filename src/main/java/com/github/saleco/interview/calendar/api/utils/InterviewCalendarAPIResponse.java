package com.github.saleco.interview.calendar.api.utils;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Interview Calendar API Response")
public class InterviewCalendarAPIResponse {

    @Schema(description = "Response Status Code", example = "1")
    private Integer statusCode;

    @Schema(description = "Response Message", example = "Some message.")
    private String message;

    public InterviewCalendarAPIResponse(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

}
