package com.leokom.games.chess.player.winboard;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.Player;
import org.mockito.stubbing.OngoingStubbing;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Leonid
 * Date-time: 22.02.15 21:27
 */
class WinboardTestGameBuilder {
	private final Player opponent;
	private final WinboardPlayer player;
	private String customResponse;

	WinboardTestGameBuilder(
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
	WinboardTestGameBuilder move( Move move ) {
		switch ( sideToMove ) {
		case WHITE:

			final String moveToReceive =
				move == Move.RESIGN ? "result 0-1 {Impl.specific reason}" : "usermove " + move.getFrom() + move.getTo();
			communicatorReceive = communicatorReceive.thenReturn( moveToReceive );
			break;
		case BLACK:
			doAnswer( invocation -> { player.opponentMoved( move ); return null; } )
					.when( opponent ).opponentMoved( lastMove );
			break;
		}

		lastMove = move;

		sideToMove = sideToMove.opposite();
		return this;
	}

	void play() {
		if ( customResponse != null ) {
			communicatorReceive = communicatorReceive.thenReturn( customResponse );
		}
		//ensure no infinite loop is continued after the last move
		communicatorReceive = communicatorReceive.thenReturn( "quit" );
		player.opponentSuggestsMeStartNewGameWhite();
	}

	Player getOpponent() {
		return opponent;
	}

	WinboardTestGameBuilder customWinboardResponse( String customResponse ) {
		this.customResponse = customResponse;
		return this;

	}
}
