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

	@Test
	public void blackPawn7Row() {
		Position position = new Position();

		//TODO: do we have guarantee characters are ordered alphabetically?
		for( char file = 'a'; file <= 'h'; file++ ) {
			testPawn( position,  file + "7", Side.BLACK, file + "6", file + "5" );
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
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( expectedMoves.length, squares.size() );
		assertEquals( new HashSet<String>( Arrays.asList( expectedMoves ) ), squares );
	}
}
