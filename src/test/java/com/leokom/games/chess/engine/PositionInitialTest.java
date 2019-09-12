package com.leokom.games.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 02.01.14 22:07
 */
public class PositionInitialTest {
	@Test
	public void getInitialPositionWhitePawn() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPawn( Side.WHITE, "a2" ) );
	}

	@Test
	public void getInitialPositionBlackPawn() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPawn( Side.BLACK, "h7" ) );
	}

	@Test
	public void knights() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPiece( Side.BLACK, "g8", PieceType.KNIGHT ) );
		assertTrue( initial.hasPiece( Side.BLACK, "b8", PieceType.KNIGHT ) );

		assertTrue( initial.hasPiece( Side.WHITE, "b1", PieceType.KNIGHT ) );
		assertTrue( initial.hasPiece( Side.WHITE, "g1", PieceType.KNIGHT ) );
	}

	@Test
	public void kings() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPiece( Side.BLACK, "e8", PieceType.KING ) );
		assertTrue( initial.hasPiece( Side.WHITE, "e1", PieceType.KING ) );
	}

	@Test
	public void queens() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPiece( Side.BLACK, "d8", PieceType.QUEEN ) );
		assertTrue( initial.hasPiece( Side.WHITE, "d1", PieceType.QUEEN ) );
	}

	@Test
	public void rooks() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPiece( Side.WHITE, "a1", PieceType.ROOK ) );
		assertTrue( initial.hasPiece( Side.BLACK, "h8", PieceType.ROOK ) );
	}

	@Test
	public void bishops() {
		final Position initial = Position.getInitialPosition();

		assertTrue( initial.hasPiece( Side.WHITE, "c1", PieceType.BISHOP ) );
		assertTrue( initial.hasPiece( Side.BLACK, "f8", PieceType.BISHOP ) );
	}
}
