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

	@Test
	public void blackCastlingKingSide() {
		Position position = new Position( null );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "h8", PieceType.ROOK );

		//target of King is unambiguously defining the type of its move
		final Position newPosition = position.move( "e8", "g8" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KING, Side.BLACK, "g8" );
		PositionAsserts.assertEmptySquare( newPosition, "e8" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.ROOK, Side.BLACK, "f8" );
		PositionAsserts.assertEmptySquare( newPosition, "h8" );
	}

	@Test
	public void blackCastlingQueenSide() {
		Position position = new Position( null );

		position.add( Side.BLACK, "e8", PieceType.KING );
		position.add( Side.BLACK, "a8", PieceType.ROOK );

		//target of King is unambiguously defining the type of its move
		final Position newPosition = position.move( "e8", "c8" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KING, Side.BLACK, "c8" );
		PositionAsserts.assertEmptySquare( newPosition, "e8" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.ROOK, Side.BLACK, "d8" );
		PositionAsserts.assertEmptySquare( newPosition, "a8" );
	}

	@Test
	public void whitef1g1NonCastling() {
		Position position = new Position( null );

		position.add( Side.WHITE, "f1", PieceType.KING );

		//target of King is unambiguously defining the type of its move
		final Position newPosition = position.move( "f1", "g1" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KING, Side.WHITE, "g1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );
		PositionAsserts.assertEmptySquare( newPosition, "f1" );
		PositionAsserts.assertEmptySquare( newPosition, "h1" );
	}

	@Test
	public void whiteLongCastling() {
		Position position = new Position( null );

		position.add( Side.WHITE, "e1", PieceType.KING );
		position.add( Side.WHITE, "a1", PieceType.ROOK );

		//target of King is unambiguously defining the type of its move
		final Position newPosition = position.move( "e1", "c1" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.KING, Side.WHITE, "c1" );
		PositionAsserts.assertEmptySquare( newPosition, "e1" );

		PositionAsserts.assertHasPiece( newPosition, PieceType.ROOK, Side.WHITE, "d1" );
		PositionAsserts.assertEmptySquare( newPosition, "a1" );
	}
}
