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
	private Position position;

	@Before
	public void prepare() {
		//TODO: this null is not important, "a" - "h" are also perfectly legal
		position = new Position( null );
	}

	@Test
	public void doubleMoveCreatesPossibility() {
		position.addPawn( Side.WHITE, "e2" );

		Position result = position.move( "e2", "e4" );

		assertEquals( "e", result.getPossibleEnPassantFile() );
	}

	@Test
	public void doubleMoveTriangulate() {
		position.addPawn( Side.WHITE, "d2" );

		Position result = position.move( "d2", "d4" );
		assertEquals( "d", result.getPossibleEnPassantFile() );
	}

	@Test
	public void singleMoveFromInitialPositionIgnored() {
		position.addPawn( Side.WHITE, "c2" );

		Position result = position.move( "c2", "c3" );
		assertEquals( null, result.getPossibleEnPassantFile() );
	}

	@Test
	public void singleMoveFromNotInitialPositionIgnored() {
		position.addPawn( Side.WHITE, "b3" );

		Position result = position.move( "b3", "b4" );
		assertEquals( null, result.getPossibleEnPassantFile() );
	}

	@Test
	public void blackPossible() {
		position.addPawn( Side.BLACK, "a7" );

		Position result = position.move( "a7", "a5" );
		assertEquals( "a", result.getPossibleEnPassantFile() );
	}

	@Test
	public void blackPossibleTriangulate() {
		position.addPawn( Side.BLACK, "g7" );
		Position result = position.move( "g7", "g5" );

		assertEquals( "g", result.getPossibleEnPassantFile() );
	}
}