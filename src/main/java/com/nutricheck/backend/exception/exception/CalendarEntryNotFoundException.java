package com.nutricheck.backend.exception.exception;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CalendarEntryNotFoundException extends RuntimeException {

    public CalendarEntryNotFoundException(String filename) {
        super("Calendar Entry not found for a filename: " + filename);
    }

    public CalendarEntryNotFoundException(LocalDate date) {
        super("Calendar Entry not found for a date: " + date);
    }


}