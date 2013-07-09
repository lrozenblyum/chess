package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Test for phrase 'not attacked by one or more of the opponent’s pieces'
 * in 3.8 a
 * Author: Leonid
 * Date-time: 09.07.13 22:17
 */
public class KingAllowedMovesCannotMoveOnAttackedSquare {
	@Test
	public void withPawnNotPromotion() {
		Position position = new Position( null );
		position.addPawn( Side.WHITE, "a2" ); //attacks b3
		position.add( Side.BLACK, "a3", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
			position, "a3",
			"a2", "a4", "b4", "b2" );

	}

	@Test
	public void withPawnPromotion() {
		Position position = new Position( null );
		position.addPawn( Side.BLACK, "h2" ); //may be promoted to h1 or capture g1
		position.add( Side.WHITE, "f1", PieceType.KING );

		PositionAsserts.assertAllowedMoves(
				position, "f1",
				"e1", "e2", "f2", "g2" ); //but not g1
	}
}
