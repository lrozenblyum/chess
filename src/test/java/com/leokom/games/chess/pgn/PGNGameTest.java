package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.player.Player;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class PGNGameTest {
    private Player whitePlayer;
    private Player blackPlayer;
    private Game game;

    @Before
    public void prepare() {
        this.whitePlayer = Mockito.mock(Player.class);
        this.blackPlayer = Mockito.mock(Player.class);
        this.game = new Game( whitePlayer, blackPlayer );
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

    @Test
    public void blackName() {
        Mockito.when( blackPlayer.name() ).thenReturn( "Some black player name" );
        String pgn = new PGNGame(new Event(null, null, null), game ).run();

        assertEquals( "[Black \"Some black player name\"]", pgn.split( "\n" )[ 5 ] );
    }

    @Test
    public void unknownPlayerNames() {
        Mockito.when( whitePlayer.name() ).thenReturn( null );
        Mockito.when( blackPlayer.name() ).thenReturn( null );
        String pgn = new PGNGame(new Event(null, null, null), game ).run();

        assertEquals( "[White \"?\"]", pgn.split( "\n" )[ 4 ] );
        assertEquals( "[Black \"?\"]", pgn.split( "\n" )[ 5 ] );
    }

    @Test
    public void whiteWins() {
        Game spyGame = Mockito.spy(new Game(whitePlayer, blackPlayer));
        Mockito.doReturn( whitePlayer ).when( spyGame ).run();

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"1-0\"]", pgn.split( "\n" )[ 6 ] );
    }

    @Test
    public void blackWins() {
        Game spyGame = Mockito.spy(new Game(whitePlayer, blackPlayer));
        Mockito.doReturn( blackPlayer ).when( spyGame ).run();

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"0-1\"]", pgn.split( "\n" )[ 6 ] );
    }


}
