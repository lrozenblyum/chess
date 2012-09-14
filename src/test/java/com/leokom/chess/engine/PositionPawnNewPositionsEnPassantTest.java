package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test en passant cases for new position generation by pawn movement
 * Author: Leonid
 * Date-time: 11.09.12 22:28
 */
public class PositionPawnNewPositionsEnPassantTest {
	//file that gives en passant right
	private static final String fileMovedBefore = "c"; //any!

	@Test
	public void doubleMoveCreatesPossibility() {
		Position position = new Position( null );
		position.addPawn( Side.WHITE, "e2" );

		Position result = position.move( "e2", "e4" );

		assertEquals( "e", result.getPossibleEnPassantFile() );
	}
}
