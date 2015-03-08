package com.leokom.chess.engine;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 08.03.15 21:15
 */
public class ResignTest {
	@Test
	public void noMoreMovesAfterResign() {
		Position position = Position.getInitialPosition();

		final Position positionAfterResign = position.move( Move.RESIGN );
		assertTrue( positionAfterResign.getMoves().isEmpty() );
	}

	@Test
	public void positionAfterResignKeepsPieces() {
		Position position = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.KING )
				.add( Side.BLACK, "h7", PieceType.QUEEN )
				.add( Side.BLACK, "h8", PieceType.KING )
				.setSideOf( "a1" )
				.build();

		Position positionAfterResign = position.move( Move.RESIGN );
		assertTrue( positionAfterResign.hasPiece( Side.WHITE, "a1", PieceType.KING ) );
	}
}
