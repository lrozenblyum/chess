package com.leokom.games.chess;

public class Event {
    private final String name;
    private final String location;

    public Event(String name, String location) {
        this.name = name;
        this.location = location;
    }

    String getName() {
        return this.name;
    }

    String getLocation() {
        return location;
    }
}
