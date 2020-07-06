package com.leokom.games.chess.pgn;

class PGNTag {
    private final String name;
    private final String value;

    PGNTag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format( "[%s \"%s\"]", name, value );
    }
}
