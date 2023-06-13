package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.GameResult;
import com.leokom.games.chess.engine.Side;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PGNGame {
    private final Event event;
    private final Game game;

    public PGNGame(Event event, Game game) {
        this.event = event;
        this.game = game;
    }

    public String run() {
        //8.1.1
        //For PGN export format, a single blank line appears after the last of the tag pairs to conclude the tag pair section.
        //This helps simple scanning programs to quickly determine the end of the tag pair section and the beginning of the movetext section.
        return sevenTagsRoster() + "\n";
    }

    private String sevenTagsRoster() {
        PGNTag eventTag = new PGNTag( "Event", (event.getName() != null ? event.getName() : "?") );
        PGNTag locationTag = new PGNTag( "Site", (event.getLocation() != null ? event.getLocation() : "?") );
        PGNTag dateTag = new PGNTag( "Date", ( event.getDate() != null ? DateTimeFormatter.ofPattern("yyyy-MM-dd").format( event.getDate() ) : "????-??-??" ) );
        PGNTag roundTag = new PGNTag( "Round", "-" );

        PGNTag whitePlayerTag = new PGNTag( "White", playerName(Side.WHITE));
        PGNTag blackPlayerTag = new PGNTag( "Black", playerName(Side.BLACK));

        PGNTag resultTag = new PGNTag( "Result", toPgnResult( game.run() ) );

        return Stream.of(eventTag, locationTag, dateTag, roundTag, whitePlayerTag, blackPlayerTag, resultTag)
                .map(PGNTag::toString)
                .collect(Collectors.joining("\n"));
    }

    private String toPgnResult( GameResult gameResult ) {
        switch ( gameResult ) {
            case WHITE_WINS:
                return "1-0";
            case BLACK_WINS:
                return "0-1";
            case DRAW:
                return "1/2-1/2";
            case UNFINISHED_GAME:
                return "*";
            default:
                throw new IllegalStateException( "Unsupported game result: " + gameResult );
        }

    }

    private String playerName(Side side) {
        return game.player(side).name() != null ? game.player(side).name() : "?";
    }
}
