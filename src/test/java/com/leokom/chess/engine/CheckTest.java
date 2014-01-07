package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Set;

import static com.leokom.chess.engine.PositionAsserts.assertAllowedMoves;
import static com.leokom.chess.engine.PositionAsserts.assertNoAllowedMoves;


/**
 * Author: Leonid
 * Date-time: 06.01.14 21:59
 */
public class CheckTest {
	@Test
	public void cannotExposeKingToCheck() {
		Position position = new Position( null );

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a2", PieceType.ROOK );
		position.add( Side.BLACK, "a3", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		assertAllowedMoves( position, "a2", "a3" ); //cannot move horizontally
	}

	@Test
	public void cannotExposeKingToCheckTriangulate() {
		Position position = new Position( null );

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a2", PieceType.BISHOP );
		position.add( Side.BLACK, "a3", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		assertNoAllowedMoves( position, "a2" );
	}

	@Test
	public void promotionCannotExposeKingToCheck() {
		Position position = new Position( null );

		position.add( Side.BLACK, "h2", PieceType.KING );
		position.add( Side.BLACK, "f2", PieceType.PAWN );

		position.add( Side.WHITE, "a2", PieceType.QUEEN ); //or rook

		assertNoAllowedMoves( position, "f2" );
	}
}
