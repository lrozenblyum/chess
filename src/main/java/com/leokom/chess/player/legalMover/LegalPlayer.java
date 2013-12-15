package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;

import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private Player opponent;
	private Position position;

	@Override
	public void run() {

	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentMoved( String opponentMove ) {
		final String[] moveParts = opponentMove.split( "-" );
		position = position.move( moveParts[ 0 ], moveParts[ 1 ] );

		Set< String[] > moves = position.getMoves( Side.WHITE );

		//TODO: if empty set
		String[] possibleMove = moves.iterator().next();

		opponent.opponentMoved( possibleMove[ 0 ] + possibleMove[ 1 ] );
		//TODO: update position when proved need
	}

	@Override
	public void opponentResigned() {

	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	//TODO: think if it's a nice injection
	public void setPosition( Position position ) {
		this.position = position;
	}
}
