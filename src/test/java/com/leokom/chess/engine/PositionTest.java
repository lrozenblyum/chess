package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * General unclassified operations
 *
 * Author: Leonid
 * Date-time: 23.03.16 22:26
 */
public class PositionTest {
	@Test
	public void enPassantIsDetectedAsCapture() {
		Position position = Position.getInitialPosition();
		final Position result = position
				.move( "e2", "e4" )
				.move( "a7", "a5" ) //any neutral
				.move( "e4", "e5" )
				.move( "f7", "f5" );//under en passant rule

		assertTrue( result.isCapture( new Move( "e5", "f6" ) ) );
	}

	@Test
	public void usualPawnMoveIsNotCapture() {
		Position position = Position.getInitialPosition();
		assertFalse( position.isCapture( new Move( "e2", "e4" ) ) );
	}

	@Test
	public void movedSideForInitialPosition() {
		//nullable for symmetry with position.getSideToMove()
		assertNull( Position.getInitialPosition().getMovedSide() );
	}
}