package com.leokom.games.chess.engine;

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

	@Test
	public void checkmateCorrectWinningSide() {
		Position position = Position.getInitialPosition();


		final Position matePosition = position
				.move( "f2", "f3" ).move( "e7", "e5" )
				.move( "g2", "g4" ).move( "d8", "h4" );

		Assert.assertEquals( Side.BLACK, matePosition.getWinningSide() );
	}
}
