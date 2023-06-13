package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.Player;
import com.leokom.games.chess.player.PlayerBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class PGNGameTest {
    private Player whitePlayer;
    private Player blackPlayer;
    private Position positionAfterTheGame;
    private Game game;

    @Before
    public void prepare() {
        this.whitePlayer = mock(Player.class);
        this.blackPlayer = mock(Player.class);
        //game at the moment depends on whitePlayer's position, the test needs to resolve this dependency
        this.positionAfterTheGame = mock( Position.class );
        Mockito.when( whitePlayer.getPosition() ).thenReturn(positionAfterTheGame);
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
        Mockito.doReturn( true ).when( positionAfterTheGame ).isTerminal();
        Mockito.doReturn(Side.WHITE ).when(positionAfterTheGame).getWinningSide();

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"1-0\"]", pgn.split( "\n" )[ 6 ] );
    }

    @Test
    public void blackWins() {
        Game spyGame = Mockito.spy(new Game(whitePlayer, blackPlayer));
        Mockito.doReturn( true ).when( positionAfterTheGame ).isTerminal();
        Mockito.doReturn( Side.BLACK ).when( positionAfterTheGame ).getWinningSide();

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"0-1\"]", pgn.split( "\n" )[ 6 ] );
    }

    @Test
    public void whiteResignsBeforeFirstMove() {
        Player whitePlayer = new PlayerBuilder(blackPlayer).move(Move.RESIGN).build();
        Game game = new Game(whitePlayer, blackPlayer);
        String pgn = new PGNGame(new Event(null, null, null), game).run();

        // -1 helps avoid ignoring trailing empty lines
        String[] pgnSplitByLines = pgn.split("\n", -1);
        assertEquals("[Result \"0-1\"]", pgnSplitByLines[6]);
        // PGN export format: a single blank line appears after the last of the tag pairs
        assertEquals("", pgnSplitByLines[7]);
    }

    @Test
    public void draw() {
        Game spyGame = Mockito.spy(new Game(whitePlayer, blackPlayer));
        Mockito.doReturn( null ).when( positionAfterTheGame ).getWinningSide();
        Mockito.doReturn( true ).when( positionAfterTheGame ).isTerminal();

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"1/2-1/2\"]", pgn.split( "\n" )[ 6 ] );
    }

    @Test
    public void unfinishedGameResultSupported() {
        Mockito.doReturn( false ).when( positionAfterTheGame ).isTerminal();

        Game spyGame = Mockito.spy(new Game(whitePlayer, blackPlayer));

        String pgn = new PGNGame(new Event(null, null, null), spyGame ).run();

        assertEquals( "[Result \"*\"]", pgn.split( "\n" )[ 6 ] );
    }

}
