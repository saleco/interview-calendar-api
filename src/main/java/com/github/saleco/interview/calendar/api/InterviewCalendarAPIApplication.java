package com.github.saleco.interview.calendar.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "Interview Calendar API", version = "1.0", description = "Interview Calendar API v1.0"))
@SpringBootApplication
public class InterviewCalendarAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewCalendarAPIApplication.class, args);
    }

}
