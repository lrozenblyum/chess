package com.leokom.chess.player.legal.evaluator.simple;

import com.leokom.chess.Game;
import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.PlayerBuilder;
import org.junit.Before;
import org.junit.Test;

import static com.leokom.chess.PlayerFactory.PlayerSelection.SIMPLE;
import static org.mockito.Mockito.*;

/**
 * Test simple player new implementation
 */
public class SimpleBrainTest {
    private Player simplePlayer;

    @Before
    public void prepare() {
        simplePlayer = SIMPLE.create();
    }

    @Test
    public void simplePlayerAcceptsDrawInMultiCaseForBlack() {
        Player whitePlayer = new PlayerBuilder( simplePlayer )
                .moveMulti( new Move( "a2", "a4" ), Move.OFFER_DRAW )
                .build();

        new Game( whitePlayer, simplePlayer ).run();

        verify( whitePlayer ).opponentMoved( Move.ACCEPT_DRAW );
    }

    @Test
    public void simplePlayerAcceptsDrawInMultiCaseForWhite() {
        Player blackPlayer = new PlayerBuilder( simplePlayer )
                .moveMulti( new Move( "d7", "d5" ), Move.OFFER_DRAW )
                .build();

        new Game( simplePlayer, blackPlayer ).run();

        verify( blackPlayer ).opponentMoved( Move.ACCEPT_DRAW );
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
    public void whiteResignOnThirdMove() {
        Player player = new PlayerBuilder( simplePlayer )
                .move( "h7", "h6" )
                .move( "g7", "g6" )
                .build();

        new Game( simplePlayer, player ).run();

        verify( player ).opponentMoved( Move.RESIGN );
    }

    @Test
    public void blackResignOnThirdMove() {
        Player player = new PlayerBuilder( simplePlayer )
                .move( "a2", "a3" )
                .move( "a3", "a4" )
                .move( "a4", "a5" )
                .build();

        new Game( player, simplePlayer ).run();

        verify( player ).opponentMoved( Move.RESIGN );
    }

    @Test
    public void noResignIfOpponentResignsOnSecondMove() {
        Player player = new PlayerBuilder( simplePlayer )
                .move( "a2", "a3" )
                .move( Move.RESIGN )
                .build();

        new Game( player, simplePlayer ).run();

        verify( player, never() ).opponentMoved( Move.RESIGN );
    }

    @Test
    public void blackOffersDrawAfterSecondMove() {
        Player player = new PlayerBuilder( simplePlayer )
                .move( "a2", "a3" )
                .move( "a3", "a4" )
                .build();

        new Game( player, simplePlayer ).run();

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