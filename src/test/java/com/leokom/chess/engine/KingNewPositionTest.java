package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 08.07.13 22:39
 */
public class KingNewPositionTest {
	@Test
	public void kingCanMove() {
		Position position = new Position( null );
		
		position.add( Side.WHITE, "e1", PieceType.KING );

		final Position newPosition = position.move( "e1", "d1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );
		PositionAsserts.assertHasPiece( newPosition,PieceType.KING, Side.WHITE, "d1" );
	}
}
