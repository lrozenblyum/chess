package com.leokom.chess.engine;

import org.junit.Ignore;
import org.junit.Test;

import static com.leokom.chess.engine.PositionAsserts.assertEmptySquare;
import static com.leokom.chess.engine.PositionAsserts.assertHasPiece;
import static com.leokom.chess.engine.Side.*;

/**
 * Author: Leonid
 * Date-time: 28.09.12 22:09
 */
public class PositionPromoteTest {
	@Test
	public void toQueen() {
		Position position = new Position( null );
		position.addPawn( WHITE, "c7" );

		Position newPosition = position.move( "c7", "c8Q" );
		assertEmptySquare( newPosition, "c7" );
		assertHasPiece( newPosition, PieceType.QUEEN, Side.WHITE, "c8" );
	}
}
