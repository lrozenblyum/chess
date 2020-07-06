package com.leokom.games.chess.pgn;

import java.time.LocalDate;

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
