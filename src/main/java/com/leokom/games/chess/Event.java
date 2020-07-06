package com.leokom.games.chess;

import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

public class Event {
    private final String name;
    private final String location;
    private final LocalDate date;

    public Event(String name, String location, LocalDate date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    String getName() {
        return this.name;
    }

    String getLocation() {
        return location;
    }

    LocalDate getDate() {
        return date;
    }
}
