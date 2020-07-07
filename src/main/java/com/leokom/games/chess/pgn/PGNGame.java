package com.leokom.games.chess.pgn;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PGNGame {
    private final Event event;

    public PGNGame(Event event) {
        this.event = event;
    }

    public String run() {
        PGNTag eventTag = new PGNTag( "Event", (event.getName() != null ? event.getName() : "?") );
        PGNTag locationTag = new PGNTag( "Site", (event.getLocation() != null ? event.getLocation() : "?") );
        PGNTag dateTag = new PGNTag( "Date", ( event.getDate() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd").format( event.getDate() ) : "????-??-??" ) );
        PGNTag roundTag = new PGNTag( "Round", "-" );

        return
            Stream.of( eventTag, locationTag, dateTag, roundTag )
            .map( PGNTag::toString )
            .collect(Collectors.joining( "\n" ) );

    }
}
