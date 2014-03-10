package com.leokom.chess.engine;

import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 16.02.14 22:02
 */
public class KingAllowedMovesCastlingTest {
	@Test
	public void castlingIsAllowed() {
		Position position = new Position( null );
		position.add( Side.WHITE, "e1", PieceType.KING );

		position.add( Side.WHITE, "h1", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e1", "g1" );
	}

	@Test
	public void castlingIsAllowedBlack() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e8", PieceType.KING );

		position.add( Side.BLACK, "h8", PieceType.ROOK );

		PositionAsserts.assertAllowedMovesInclude(
				position, "e8", "g8" );
	}
}
