package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Set;

import static com.leokom.chess.engine.PositionAsserts.assertAllowedMoves;


/**
 * Author: Leonid
 * Date-time: 06.01.14 21:59
 */
public class CheckTest {
	@Test
	public void cannotExposeKingToCheck() {
		Position position = new Position( null );

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a2", PieceType.ROOK ); //cannot move it!
		position.add( Side.BLACK, "a3", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		assertAllowedMoves( position, "a2" );
	}
}
