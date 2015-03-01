package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test en passant cases for new position generation by pawn movement
 * Author: Leonid
 * Date-time: 11.09.12 22:28
 */
public class PositionEnPassantPossibilityCreationTest {
	private PositionBuilder position;

	@Before
	public void prepare() {
		position = new PositionBuilder();
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
		assertNull( result.getPossibleEnPassantFile() );
	}

	@Test
	public void singleMoveFromNotInitialPositionIgnored() {
		position.addPawn( Side.WHITE, "b3" );

		Position result = position.move( "b3", "b4" );
		assertNull( result.getPossibleEnPassantFile() );
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

	@Test
	public void blackSingleMoveFromNotInitial() {
		position.addPawn( Side.BLACK, "g6" );

		Position result = position.move( "g6", "g5" );
		assertNull( result.getPossibleEnPassantFile() );
	}

	@Test
	public void blackInitialSingleMove() {
		position.addPawn( Side.BLACK, "c7" );

		Position result = position.move( "c7", "c6" );
		assertNull( result.getPossibleEnPassantFile() );
	}

	@Test
	public void flagIsNotPreservedNextMove() {
		position.setEnPassantFile( "e" );
		position.addPawn( Side.WHITE, "e4" );

		position.addPawn( Side.BLACK, "c7" ); //any
		//any not double-pawn move
		Position result = position.move( "c7", "c6" );
		assertNull( result.getPossibleEnPassantFile() );
	}

	@Test
	public void captureIgnored() {
		position.addPawn( Side.BLACK, "h7" );
		position.addPawn( Side.WHITE, "g6" );

		Position result = position.move( "h7", "g6" );
		assertNull( result.getPossibleEnPassantFile() );
	}
}