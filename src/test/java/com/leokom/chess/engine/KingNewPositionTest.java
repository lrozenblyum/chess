package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 08.07.13 22:39
 */
public class KingNewPositionTest {
	@Test
	public void kingCanMove() {
		Position position = new Position( null );
		
		position.add( Side.WHITE, "e1", PieceType.KING );

		final Position newPosition = position.move( "e1", "d1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );
		PositionAsserts.assertHasPiece( newPosition,PieceType.KING, Side.WHITE, "d1" );
	}

	@Test
	public void capture() {
		Position position = new Position( null );

		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.BLACK, "d1", PieceType.QUEEN );

		final Position newPosition = position.move( "e1", "d1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );
		PositionAsserts.assertHasPiece( newPosition,PieceType.KING, Side.WHITE, "d1" );
	}

	@Test
	public void whiteShortCastling() {
		Position position = new Position( null );

		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "h1", PieceType.ROOK );

		//TODO: do we need special second argument for castling?
		//target of King is unambiguously defining the type of its move
		final Position newPosition = position.move( "e1", "g1" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KING, Side.WHITE, "g1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.ROOK, Side.WHITE, "f1" );
		PositionAsserts.assertEmptySquare( newPosition, "h1" );
	}
}
