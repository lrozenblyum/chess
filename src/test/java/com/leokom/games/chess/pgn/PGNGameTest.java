package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.player.Player;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PGNGameTest {
    private Player whitePlayer;
    private Game game;

    @Before
    public void prepare() {
        this.whitePlayer = Mockito.mock(Player.class);
        this.game = new Game( whitePlayer, Mockito.mock( Player.class ) );
    }

    @Test
    public void eventTag() {
        String pgn = new PGNGame(new Event(null, null, null), game).run();
        assertThat( pgn,
            CoreMatchers.startsWith( "[Event \"?\"]" )
        );
    }

    @Test
    public void eventNameKnown() {
        String pgn = new PGNGame( new Event( "Good event", null, null), game).run();
        assertEquals( "[Event \"Good event\"]", pgn.split( "\n" )[ 0 ] );
    }

    @Test
    public void lineSeparators() {
        String pgn = new PGNGame(new Event("Good event", null, null), game).run();
        assertThat( pgn, CoreMatchers.containsString( "\n" ));
    }

    @Test
    public void locationUnknown() {
        String pgn = new PGNGame(new Event(null, null, null), game).run();
        assertEquals( "[Site \"?\"]", pgn.split( "\n" )[ 1 ] );
    }

    @Test
    public void locationKnown() {
        String pgn = new PGNGame(new Event(null, "New York City, NY USA", null), game).run();
        assertEquals( "[Site \"New York City, NY USA\"]", pgn.split( "\n" )[ 1 ] );
    }

    @Test
    public void dateKnown() {
        String pgn = new PGNGame(new Event(null, null, LocalDate.of(2020, 7, 5)), game).run();
        assertEquals( "[Date \"2020-07-05\"]", pgn.split( "\n" )[ 2 ] );
    }

    @Test
    public void hyphenNotAppropriate() {
        String pgn = new PGNGame(new Event(null, null, null), game).run();
        assertEquals( "[Round \"-\"]", pgn.split( "\n" )[ 3 ] );
    }

    @Test
    public void whiteName() {
        Mockito.when( whitePlayer.name() ).thenReturn( "White player name" );
        String pgn = new PGNGame(new Event(null, null, null), game ).run();

        assertEquals( "[White \"White player name\"]", pgn.split( "\n" )[ 4 ] );
    }

}
