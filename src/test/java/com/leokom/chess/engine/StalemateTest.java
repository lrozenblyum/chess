package com.leokom.chess.engine;

import org.junit.Assert;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Author: Leonid
 * Date-time: 25.07.16 19:44
 */
public class StalemateTest {
	@Test
	public void stalematePositionIsTerminal() {
		final Position position = new PositionBuilder()
				.add( Side.WHITE, "d5", PieceType.KING )
				.add( Side.WHITE, "d7", PieceType.PAWN )
				.add( Side.BLACK, "d8", PieceType.KING )
				.build();

		final Position result = position.move( "d5", "d6" );

		Assert.assertTrue( result.isTerminal() );
		assertNull( result.getWinningSide() );
		assertNull( result.getSideToMove() );
	}

	@Test
	public void mirrorToStalemateIsNotStalemate() {
		final Position position = new PositionBuilder()
				.add( Side.WHITE, "d6", PieceType.KING )
				.add( Side.WHITE, "d7", PieceType.PAWN )
				.add( Side.BLACK, "e8", PieceType.KING )
				.setSide( Side.BLACK )
				.build();

		final Position result = position.move( "e8", "d8" );

		assertFalse( result.isTerminal() );
		assertEquals( Side.WHITE, result.getSideToMove() );
	}
}
