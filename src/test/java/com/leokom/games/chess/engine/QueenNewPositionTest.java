package com.leokom.games.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 06.07.13 19:39
 */
public class QueenNewPositionTest {
	@Test
	public void queenMove() {
		PositionBuilder position = new PositionBuilder();
		position.addQueen( Side.BLACK, "a2" );

		position.addPawn( Side.WHITE, "c4" );

		final Position newPosition = position.move( "a2", "c4" );

		PositionAsserts.assertEmptySquare( newPosition, "a2" );
		PositionAsserts.assertHasPiece( newPosition, PieceType.QUEEN, Side.BLACK, "c4" );


	}
}
