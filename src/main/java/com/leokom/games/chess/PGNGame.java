package com.leokom.games.chess;

import java.time.format.DateTimeFormatter;

public class PGNGame {
    private final Event event;

    public PGNGame(Event event) {
        this.event = event;
    }

    public String run() {
        return "[Event \"" +
                (event.getName() != null ? event.getName() : "?") +
                "\"]\n" +
                "[Site \"" +
                (event.getLocation() != null ? event.getLocation() : "?") +
                "\"]\n" +
                "[Date \"" +
                ( event.getDate() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd").format( event.getDate() ) : "????-??-??" ) +
                "\"]";
    }
}
