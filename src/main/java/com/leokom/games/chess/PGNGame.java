package com.leokom.games.chess;

public class PGNGame {
    private final Event event;

    public PGNGame(Event event) {
        this.event = event;
    }

    public String run() {
        return "[Event \"" +
                ( event.getName() != null ? event.getName() : "?" ) +
                "\"]\n";
    }
}
