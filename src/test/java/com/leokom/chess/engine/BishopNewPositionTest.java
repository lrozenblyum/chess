package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 30.06.13 19:39
 */
public class BishopNewPositionTest {
	@Test
	public void moveBishop() {
		Position position = new Position( Side.BLACK );
		position.addPawn( Side.WHITE, "a1" );
		position.add( Side.BLACK, "h8", PieceType.BISHOP );

		final Position captured = position.move( "h8", "a1" );
		assertTrue( captured.hasPiece( Side.BLACK, "a1", PieceType.BISHOP ) );
	}
}
