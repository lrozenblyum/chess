package com.leokom.chess.player.simple;

import com.leokom.chess.Game;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.PlayerBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test simple player before merging it into LegalPlayer
 */
public class SimplePlayerTest {
    private SimplePlayer simplePlayer;

    @Before
    public void prepare() {
        simplePlayer = new SimplePlayer();
    }

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
        Player player = mock( Player.class );
        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( new Move( "e2", "e4" ) );
    }

    @Test
    public void blackFirstMove() {
        Player player = new PlayerBuilder( simplePlayer, Side.WHITE ).move( "a1", "a2" ).build();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( new Move( "e7", "e5" ) );
    }

    @Test
    public void whiteSecondMove() {
        Player player = new PlayerBuilder( simplePlayer, Side.BLACK )
            .move( "h7", "h6" )
            .build();

        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( new Move( "d2", "d4" ) );
    }
}