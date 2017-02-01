package com.leokom.chess.player.simple;

import com.leokom.chess.Game;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.Player;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test simple player before merging it into LegalPlayer
 */
public class SimplePlayerTest {
    /*
     * Simple player's behaviour:
     * a) first move : e pawn 2 squares forward
     * b) always accept draw if the opponent offers it
     * c) second move : d pawn 2 squares forward
     * d) after 2'nd move : offer draw
     * e) 3'd move : resign
     */
    @Test
    public void whiteFirstMove() {
        SimplePlayer simplePlayer = new SimplePlayer();
        Player player = mock( Player.class );
        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( new Move( "e2", "e4" ) );
    }

    @Test
    public void blackFirstMove() {
        Player player = mock( Player.class );
        SimplePlayer simplePlayer = new SimplePlayer();
        //setting up the position due to assymetry from 'Game'
        when( player.getPosition() ).thenReturn( Position.getInitialPosition() );
        doAnswer( invocationOnMock -> {
            simplePlayer.opponentMoved( new Move("a1", "a2") );
            return null;
        } ).when( player ).opponentSuggestsMeStartNewGameWhite();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( new Move( "e7", "e5" ) );
    }
}