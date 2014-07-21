package com.leokom.chess.engine;

import org.junit.Test;

import static com.leokom.chess.engine.PawnUtils.testPawn;

/**
 * Idea of class - thorough testing of some cases by looping over different positions
 * (in contrary to @see PawnAllowedMovesTest)
 *
 * Author: Leonid
 * Date-time: 28.08.12 21:14
 */
public class PawnAllowedMovesMultiplePositionTest {

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void blackPawn7rank() {
		//TODO: do we have guarantee characters are ordered alphabetically?
		for( char file = 'a'; file <= 'h'; file++ ) {
			Position position = new Position();
			testPawn( position, file + "7", Side.BLACK, file + "6", file + "5" );
		}
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void blackPawnNotInitialPosition() {
		for( char file = 'a'; file <= 'h'; file++ ) {
			//1, 8 aren't possible
			//2 is source of promotion rules (so we'll test it separately)
			//7 -> 2 destinations.
			for ( int rank = 3; rank <= 6; rank++ ) {
				Position position = new Position();
				final String sourceRank = String.valueOf( rank );
				final String expectedRank = String.valueOf( rank - 1 );

				testPawn( position, file + sourceRank, Side.BLACK, file + expectedRank );
			}
		}
	}
}
