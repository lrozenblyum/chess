package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class PositionPawnTest {
	/**
	 * FIDE 3.7b
	 */
	@Test
	public void singlePawnInitialPosition() {
		Position position = new Position();
		testPawn( position, "e2", Side.WHITE, "e3", "e4" );
	}

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void singlePawnAnother() {
		Position position = new Position();

		testPawn( position, "d2", Side.WHITE, "d3", "d4" );
	}

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void blackPawn7Row() {
		//TODO: do we have guarantee characters are ordered alphabetically?
		for( char file = 'a'; file <= 'h'; file++ ) {
			Position position = new Position();
			testPawn( position,  file + "7", Side.BLACK, file + "6", file + "5" );
		}
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void blackPawnNotInitialPosition() {
		for( char file = 'a'; file <= 'h'; file++ ) {
			//1, 8 aren't possible
			//2 is source of promotion rules. TODO: and what?? The square is accessible anyway!
			//7 -> 2 destinations.
			for ( int row = 3; row <= 6; row++ ) {
				Position position = new Position();
				String sourceRow = String.valueOf( row );
				String expectedRow = String.valueOf( row - 1 );

				testPawn( position, file + sourceRow, Side.BLACK, file + expectedRow );
			}
		}
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMove() {
		Position position = new Position();

		testPawn( position, "d3", Side.WHITE, "d4" );
	}

	@Test
	public void singleCapturePossibleFromWhite() {
		Position position = new Position();
		position.addPawn( Side.WHITE, "d2" );
		position.addPawn( Side.BLACK, "e3" );

		Set<String> allowedMoves = position.getMovesFrom( "d2" );

		//TODO: think if capture must be returned as just e3 or as e3Capture?
		assertAllowedMoves( position, "d2", "d3", "d4", "e3" );
	}

	@Test
	public void singleCaptureImPossibleFromWhiteColorsCoincide() {
		Position position = new Position();
		position.addPawn( Side.WHITE, "d2" );
		position.addPawn( Side.WHITE, "e3" ); //our color - cannot capture for sure!

		Set<String> allowedMoves = position.getMovesFrom( "d2" );

		assertAllowedMoves( position, "d2", "d3", "d4" );
	}

	@Test
	public void captureFromNotInitialPositionRightSideCapture() {
		Position position = new Position();
		position.addPawn( Side.WHITE, "g5" );
		position.addPawn( Side.BLACK, "h6" );

		Set<String> allowedMoves = position.getMovesFrom( "g5" );
		assertAllowedMoves( position, "g6", "h6" );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMoveSecondTry() {
		Position position = new Position();
		testPawn( position, "a4", Side.WHITE, "a5" );
	}

	private void testPawn( Position position, String initialField, Side side, String... expectedMoves ) {
		position.addPawn( side, initialField );
		assertAllowedMoves( position, initialField, expectedMoves );
	}

	private void assertAllowedMoves( Position position, String initialField, String... expectedMoves ) {
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( expectedMoves.length, squares.size() );
		assertEquals( new HashSet<String>( Arrays.asList( expectedMoves ) ), squares );
	}
}
