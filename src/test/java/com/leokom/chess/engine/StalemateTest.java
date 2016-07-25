package com.leokom.chess.engine;

import org.junit.Assert;
import org.junit.Test;

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
	}
}
