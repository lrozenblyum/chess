package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Leonid
 * Date-time: 22.02.15 21:27
 */
public class WinboardTestGameBuilder {
	private final Player opponent;
	private final WinboardPlayer player;
	public WinboardTestGameBuilder(
		WinboardPlayer player,
		WinboardCommunicator communicator ) {
		this.player = player;
		this.opponent =	mock( Player.class );

		communicatorReceive = when( communicator.receive() );

		player.setOpponent( opponent );
	}

	private Move lastMove;
	private Side sideToMove = Side.WHITE;

	//Mockito feature to program consecutive calls
	private OngoingStubbing<String> communicatorReceive;

	//highly depends on knowledge that Winboard: White, opponent Black
	public WinboardTestGameBuilder move( Move move ) {
		switch ( sideToMove ) {
		case WHITE:
			communicatorReceive = communicatorReceive.thenReturn( "usermove " + move.getFrom() + move.getTo() );
			break;
		case BLACK:
			doAnswer( invocation -> { player.opponentMoved( move ); return null; } )
					.when( opponent ).opponentMoved( lastMove );
		}

		lastMove = move;

		sideToMove = sideToMove.opposite();
		return this;
	}

	//not very 'nice' to force caller use this method
	//but for now it's simpler than keeping more state in the builder
	public WinboardTestGameBuilder moveLast( Move move ) {
		switch ( sideToMove ) {
			case WHITE:
				communicatorReceive = communicatorReceive.
						thenAnswer( invocation -> {
							player.applyShuttingDown();
							return "usermove " + move.getFrom() + move.getTo();
						} )
						.thenReturn( "" );

				break;
			case BLACK:
				//ensure no infinite loop is continued after the last move
				doAnswer( invocation -> { player.opponentMoved( move ); player.applyShuttingDown(); return null; } )
						.when( opponent ).opponentMoved( lastMove );

				communicatorReceive = communicatorReceive.thenReturn( "" );
				break;
		}

		return this;
	}

	public void play() {
		player.opponentSuggestsMeStartNewGameWhite();
	}
}
