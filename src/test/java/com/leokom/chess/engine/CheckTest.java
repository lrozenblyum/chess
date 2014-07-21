package com.leokom.chess.engine;

import org.junit.Test;

import java.util.Set;

import static com.leokom.chess.engine.PositionAsserts.assertAllowedMoves;
import static com.leokom.chess.engine.PositionAsserts.assertNoAllowedMoves;
import static org.junit.Assert.assertEquals;


/**
 * Author: Leonid
 * Date-time: 06.01.14 21:59
 */
public class CheckTest {
	@Test
	public void cannotExposeKingToCheck() {
		Position position = new Position();

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a2", PieceType.ROOK );
		position.add( Side.BLACK, "a3", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		assertAllowedMoves( position, "a2", "a3" ); //cannot move horizontally
	}

	@Test
	public void cannotExposeKingToCheckTriangulate() {
		Position position = new Position();

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a2", PieceType.BISHOP );
		position.add( Side.BLACK, "a3", PieceType.ROOK );

		position.add( Side.BLACK, "h8", PieceType.KING );

		assertNoAllowedMoves( position, "a2" );
	}

	@Test
	public void promotionCannotExposeKingToCheck() {
		Position position = new Position();

		position.add( Side.BLACK, "h2", PieceType.KING );
		position.add( Side.BLACK, "f2", PieceType.PAWN );

		position.add( Side.WHITE, "a2", PieceType.QUEEN ); //or rook

		assertNoAllowedMoves( position, "f2" );
	}

	//more integration test - checking whole board moves
	@Test
	public void cannotLeaveKingInCheck() {
		Position position = new Position();

		position.add( Side.BLACK, "h2", PieceType.KING );
		position.add( Side.WHITE, "g2", PieceType.QUEEN );
		position.add( Side.BLACK, "a1", PieceType.QUEEN );


		final Set<Move> moves = position.getMoves( Side.BLACK );
		assertEquals( 1, moves.size() );
		final Move singleMove = moves.iterator().next();
		assertEquals( "h2", singleMove.getFrom() );
		assertEquals( "g2", singleMove.getTo() );
	}

	//even if such pieces are constrained from moving to that square because they would then leave or place their own king in check
	@Test
	public void checkEvenIfCannotMove() {
		Position position = new Position();

		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.WHITE, "a3", PieceType.ROOK );

		//checking white king but cannot move itself due to exposing black king to check
		position.add( Side.BLACK, "c3", PieceType.BISHOP );
		position.add( Side.BLACK, "d3", PieceType.KING );

		assertAllowedMoves( position, "a3", "c3" ); //cannot move b3 and a file
	}
}
