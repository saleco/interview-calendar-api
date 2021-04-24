package com.github.saleco.interview.calendar.api.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DateMapperTest {

    private DateMapper dateMapper;

    @BeforeEach
    void setUp() {
        this.dateMapper = new DateMapper();
    }

    @DisplayName("Given null timestamp when asOffsetDateTime then it should return null")
    @Test
    void givenNullTimestampWhenAsOffsetDateTimeThenItShouldReturnNull() {
        Assertions.assertNull(dateMapper.asOffsetDateTime(null));
    }


    @DisplayName("Given valid timestamp when asOffsetDateTime then it should return OffsetDateTime")
    @Test
    void givenValidTimestampWhenAsOffsetDateTimeThenItShouldReturnOffsetDateTime() {
        OffsetDateTime offsetDateTime = dateMapper.asOffsetDateTime(Timestamp.from(Instant.now()));
        assertThat(offsetDateTime).isNotNull();
    }

    @DisplayName("Given null OffsetDateTime when asTimestamp then it should return null")
    @Test
    void givenNullOffsetDateTimeWhenAsTimestampThenItShouldReturnNull() {
        Assertions.assertNull(dateMapper.asTimestamp(null));
    }


    @DisplayName("Given valid OffsetDateTime when asTimestamp then it should return Timestamp")
    @Test
    void givenValidOffsetDateTimeWhenAsTimestampThenItShouldReturnTimestamp() {
        Timestamp timestamp = dateMapper.asTimestamp(OffsetDateTime.now());
        assertThat(timestamp).isNotNull();
    }
}