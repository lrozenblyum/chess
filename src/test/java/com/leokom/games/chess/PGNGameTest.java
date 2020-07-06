package com.leokom.games.chess;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PGNGameTest {
    @Test
    public void eventTag() {
        String pgn = new PGNGame(new Event(null, null, null)).run();
        assertThat( pgn,
            CoreMatchers.startsWith( "[Event \"?\"]" )
        );
    }

    @Test
    public void eventNameKnown() {
        String pgn = new PGNGame( new Event( "Good event", null, null) ).run();
        assertEquals( "[Event \"Good event\"]", pgn.split( "\n" )[ 0 ] );
    }

    @Test
    public void lineSeparators() {
        String pgn = new PGNGame(new Event("Good event", null, null)).run();
        assertThat( pgn, CoreMatchers.containsString( "\n" ));
    }

    @Test
    public void locationUnknown() {
        String pgn = new PGNGame(new Event(null, null, null)).run();
        assertEquals( "[Site \"?\"]", pgn.split( "\n" )[ 1 ] );
    }

    @Test
    public void locationKnown() {
        String pgn = new PGNGame(new Event(null, "New York City, NY USA", null)).run();
        assertEquals( "[Site \"New York City, NY USA\"]", pgn.split( "\n" )[ 1 ] );
    }

    @Test
    public void dateKnown() {
        String pgn = new PGNGame(new Event(null, null, LocalDate.of(2020, 7, 5))).run();
        assertEquals( "[Date \"2020-07-05\"]", pgn.split( "\n" )[ 2 ] );
    }

}
