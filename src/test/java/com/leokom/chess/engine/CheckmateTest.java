package com.leokom.chess.engine;

import org.junit.Assert;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 08.02.15 18:27
 */
public class CheckmateTest {
	@Test
	public void noMoreMovesAfterCheckmate() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "h3", PieceType.QUEEN );
		position.add( Side.WHITE, "c1", PieceType.KING );
		position.add( Side.BLACK, "a1", PieceType.KING );

		final Position matePosition = position.move( new Move( "h3", "a3" ) );

		Assert.assertTrue( matePosition.getMoves().isEmpty() );
	}
}
