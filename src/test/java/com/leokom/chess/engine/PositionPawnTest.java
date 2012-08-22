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
		testWhitePawnInitially( position, "e2", "e3", "e4" );
	}

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void singlePawnAnother() {
		Position position = new Position();

		testWhitePawnInitially( position, "d2", "d3", "d4" );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMove() {
		Position position = new Position();

		testWhitePawnInitially( position, "d3", "d4" );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMoveSecondTry() {
		Position position = new Position();
		testWhitePawnInitially( position, "a4", "a5" );
	}

	private void testWhitePawnInitially( Position position, String initialField, String... expectedMoves ) {
		position.addPawn( Side.WHITE, initialField );
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( expectedMoves.length, squares.size() );
		assertEquals( new HashSet<String>( Arrays.asList( expectedMoves ) ), squares );
	}
}
