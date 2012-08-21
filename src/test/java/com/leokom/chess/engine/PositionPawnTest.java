package com.leokom.chess.engine;

import org.junit.Test;

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
		position.addPawn( Side.WHITE, "e2" );

		Set<String> squares = position.getMovesFrom( "e2" );

		assertEquals( 2, squares.size() );
		assertTrue( squares.contains( "e3" ) );
		assertTrue( squares.contains( "e4" ) );
	}

	@Test
	public void singlePawnAnother() {
		Position position = new Position();
		position.addPawn( Side.WHITE, "d2" );

		Set<String> squares = position.getMovesFrom( "d2" );

		assertEquals( 2, squares.size() );
		assertTrue( squares.contains( "d3" ) );
		assertTrue( squares.contains( "d4" ) );
	}
}
