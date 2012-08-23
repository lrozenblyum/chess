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
	public void blackPawn7rank() {
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
			for ( int rank = 3; rank <= 6; rank++ ) {
				Position position = new Position();
				String sourcerank = String.valueOf( rank );
				String expectedrank = String.valueOf( rank - 1 );

				testPawn( position, file + sourcerank, Side.BLACK, file + expectedrank );
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
		addCapturable( position, Side.BLACK, "e3" );

		//TODO: think if capture must be returned as just e3 or as e3Capture?
		assertAllowedMoves( position, "d2", "d3", "d4", "e3" );
	}

	@Test
	public void singleCaptureImPossibleFromWhiteColorsCoincide() {
		Position position = new Position();
		position.addPawn( Side.WHITE, "d2" );
		addCapturable( position, Side.WHITE, "e3" );    //our color - cannot capture for sure!

		assertAllowedMoves( position, "d2", "d3", "d4" );
	}

	@Test
	public void captureFromNotInitialPositionRightSide() {
		final String sourceSquare = "g5";
		final String victimPawnSquare = "h6";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );
		addCapturable( position, Side.BLACK, victimPawnSquare );

		assertAllowedMoves( position, sourceSquare, "g6", victimPawnSquare );
	}

	@Test
	public void rightMostFileCapturingAtRightSideImpossible() {
		final String sourceSquare = "h4";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );

		assertAllowedMoves( position, sourceSquare, "h5" );
	}

	@Test
	public void leftCapture() {
		final String sourceSquare = "d6";
		final String victimSquare = "c7";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );

		addCapturable( position, Side.BLACK, victimSquare );

		assertAllowedMoves( position, sourceSquare, "d7", victimSquare );
	}

	@Test
	public void leftMostCaptureAbsent() {
		final String sourceSquare = "a3";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );

		assertAllowedMoves( position, sourceSquare, "a4" );
	}

	@Test
	public void twoSidedCapture() {
		final String sourceSquare = "f6";
		final String firstVictim = "e7";
		final String secondVictim = "g7";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );

		addCapturable( position, Side.BLACK, firstVictim );

		addCapturable( position, Side.BLACK, secondVictim );
		assertAllowedMoves( position, sourceSquare, "f7", firstVictim, secondVictim );
	}

	//maximally possible squares to move by pawn
	@Test
	public void twoSidedCaptureFromInitialPosition() {
		final String sourceSquare = "b2";
		final String firstVictim = "c3";
		final String secondVictim = "a3";

		Position position = new Position();
		position.addPawn( Side.WHITE, sourceSquare );
		addCapturable( position, Side.BLACK, firstVictim );
		addCapturable( position, Side.BLACK, secondVictim );

		assertAllowedMoves( position, sourceSquare, "b3", "b4", firstVictim, secondVictim );
	}

	@Test
	public void blackRightCapture() {
		final String source = "g7";
		final String victim = "h6";

		Position position = new Position();
		position.addPawn( Side.BLACK, source );
		addCapturable( position, Side.WHITE, source );

		assertAllowedMoves( position, source, "g6", "h6" );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMoveSecondTry() {
		Position position = new Position();
		testPawn( position, "a4", Side.WHITE, "a5" );
	}


	//TODO: point to extend! When we introduce new pieces - need to make here randomization
	//over each piece that can be captured (all except King!)
	private static void addCapturable( Position position, Side side, String square ) {
		position.addPawn( side, square );
	}

	private void testPawn( Position position, String initialField, Side side, String... reachableSquares ) {
		position.addPawn( side, initialField );
		assertAllowedMoves( position, initialField, reachableSquares );
	}

	/**
	 * Check that inside the position, starting from initial field,
	 * we can legally reach EVERY reachableSquares
	 * (basing on position's feedback)
	 * @param position
	 * @param initialField
	 * @param reachableSquares
	 */
	private void assertAllowedMoves( Position position, String initialField, String... reachableSquares ) {
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( reachableSquares.length, squares.size() );
		assertEquals( new HashSet<String>( Arrays.asList( reachableSquares ) ), squares );
	}
}
