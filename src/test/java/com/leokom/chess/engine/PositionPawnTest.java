package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class PositionPawnTest {
	@Test
	public void singlePawnInitialPosition() {
		Position position = new Position();
		testWhitePawnInitially( position, "e2", "e3", "e4" );
	}

	@Test
	public void singlePawnAnother() {
		Position position = new Position();

		testWhitePawnInitially( position, "d2", "d3", "d4" );
	}

	private void testWhitePawnInitially( Position position, String initialField, String... expectedMoves ) {
		position.addPawn( Side.WHITE, initialField );
		Set<String> squares = position.getMovesFrom( initialField );
		assertEquals( 2, squares.size() );
		assertEquals( squares, new HashSet<String>( Arrays.asList( expectedMoves ) ) );
	}
}
