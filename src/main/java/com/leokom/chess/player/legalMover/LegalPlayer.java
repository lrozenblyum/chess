package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.PieceType;
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
	private Position position = getInitialPosition();

	private static Position getInitialPosition() {
		final Position result = new Position( null );
		result.add( Side.WHITE, "e2", PieceType.PAWN );
		result.add( Side.BLACK, "d7", PieceType.PAWN );
		result.add( Side.BLACK, "e7", PieceType.PAWN );
		return result;
	}

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
		//TODO: null is a hidden ugly way to say 'it's our first move now'
		if ( opponentMove != null ) {
			//TODO: hard dependency on NOT-INTERNAL format (Winboard?)
			//TODO: castling etc will cause crash here?
			String source = opponentMove.substring( 0, 2 );
			String destination = opponentMove.substring( 2, 4 );

			position = position.move( source, destination );
		}

		Set< String[] > moves = position.getMoves( Side.WHITE );

		//TODO: if empty set it means the game has been finished (what's the result?)
		if ( !moves.isEmpty() ) {
			String[] possibleMove = moves.iterator().next();

			opponent.opponentMoved( possibleMove[ 0 ] + possibleMove[ 1 ] );
			//TODO: update position when proved need
		}
		//TODO: else?

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
