package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test en passant cases for new position generation by pawn movement
 * Author: Leonid
 * Date-time: 11.09.12 22:28
 */
public class PositionEnPassantPossibilityCreationTest {
	//file that gives en passant right
	private static final String fileMovedBefore = "c"; //any!

	@Test
	public void doubleMoveCreatesPossibility() {
		Position position = getEmptyPosition();

		position.addPawn( Side.WHITE, "e2" );

		Position result = position.move( "e2", "e4" );

		assertEquals( "e", result.getPossibleEnPassantFile() );
	}

	@Test
	public void doubleMoveTriangulate() {
		Position position = getEmptyPosition();
		position.addPawn( Side.WHITE, "d2" );

		Position result = position.move( "d2", "d4" );
		assertEquals( "d", result.getPossibleEnPassantFile() );
	}

	@Test
	public void singleMoveFromInitialPositionIgnored() {
		Position position = getEmptyPosition();
		position.addPawn( Side.WHITE, "c2" );

		Position result = position.move( "c2", "c3" );
		assertEquals( null, result.getPossibleEnPassantFile() );
	}

	private Position getEmptyPosition() {
		//TODO: this null is not important, "a" - "h" are also perfectly legal
		return new Position( null );
	}
}