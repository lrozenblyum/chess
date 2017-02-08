package com.leokom.chess.player;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

/**
 *
 * Created by Leonid on 07.02.17.
 */
//create players for test purposes
//particularly similar to WinboardTestGameBuilder
public class PlayerBuilder {
    private final Player opponent;
    private final Side side;
    private Player player;

    private Move moveToExecute;
    private Position position;

    public PlayerBuilder( Player opponent, Side ourSide ) {
        this.player = Mockito.mock( Player.class );
        this.opponent = opponent;
        this.side = ourSide;
        this.position = Position.getInitialPosition();

        updatePositionByOpponentMove();
        //TODO: it will return the original position due to immutability
        when( player.getPosition() ).thenReturn( position );
    }

    private void updatePositionByOpponentMove() {
        ArgumentCaptor< Move > opponentMoveCaptor = ArgumentCaptor.forClass( Move.class );

        doAnswer( invocationOnMock -> {
            position = position.move( opponentMoveCaptor.getValue() );

            if ( moveToExecute != null && side == Side.BLACK ) {
                doMove();
            }

            return null;
        } ).when( player ).opponentMoved( opponentMoveCaptor.capture() );
    }

    public PlayerBuilder move( Move move ) {
        this.moveToExecute =  move;
        return this;
    }

    public PlayerBuilder move( String from, String to ) {
        return this.move( new Move( from, to ) );
    }

    public Player build() {
        if ( moveToExecute != null && side == Side.WHITE ) {
            doAnswer(invocationOnMock -> {
                doMove();
                return null;
            }).when(player).opponentSuggestsMeStartNewGameWhite();
        }

        return player;
    }

    private void doMove() {
        Move toBeDone = moveToExecute;
        position = position.move( toBeDone );
        //next time we don't want the mock invoked again
        moveToExecute = null;
        opponent.opponentMoved( toBeDone );
    }
}
