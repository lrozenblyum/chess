package com.leokom.chess.player.simple;

import com.leokom.chess.Game;
import com.leokom.chess.engine.Move;
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
        Player player = new PlayerBuilder( simplePlayer ).move( "a1", "a2" ).build();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( new Move( "e7", "e5" ) );
    }

    @Test
    public void whiteSecondMove() {
        Player player = new PlayerBuilder( simplePlayer )
            .move( "h7", "h6" )
            .build();

        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( new Move( "d2", "d4" ) );
    }

    @Test
    public void whiteOffersDrawAfterSecondMove() {
        Player player = new PlayerBuilder( simplePlayer)
                .move( "h7", "h6" )
                .build();

        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( Move.OFFER_DRAW );
    }

    @Test
    public void blackSecondMove() {
        Player player =
            new PlayerBuilder( simplePlayer)
            .move( "b2", "b3" )
            .move( "c2", "c3" )
            .build();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( new Move( "d7", "d5" ) );
    }

    @Test
    public void offerDrawIsAcceptedWhenPlayingWhite() {
        Player player = new PlayerBuilder( simplePlayer)
                .move( Move.OFFER_DRAW )
                .build();

        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( Move.ACCEPT_DRAW );
    }

    @Test
    public void offerDrawIsAcceptedWhenPlayingBlack() {
        Player player = new PlayerBuilder( simplePlayer)
                .move( Move.OFFER_DRAW )
                .build();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( Move.ACCEPT_DRAW );
    }
}