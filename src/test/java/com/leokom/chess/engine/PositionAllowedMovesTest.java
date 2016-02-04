package com.leokom.chess.engine;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 14.12.13 14:01
 */
public class PositionAllowedMovesTest {
	// 1.2. ’capturing’ the opponent’s king .. not allowed

	//technically in reality this might not happen
	//opponent cannot leave its king under check
	//anyway our chess engine mustn't act against rules even if opponent did
	//even more - FIDE rules have an explicit statement about the king capture.
	@Test
	public void cannotCaptureKing() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "a1", PieceType.QUEEN );
		position.add( Side.BLACK, "c1", PieceType.KING );

		PositionAsserts.assertAllowedMovesOmit( position, "a1", "c1" );
	}

	@Test
	public void simplePositionSingleMove() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.BLACK, "c1", PieceType.KING );

		Set< Move > moves = position.getNormalMoves();

		assertEquals( 1, moves.size() );
		final Move move = moves.iterator().next();
		assertEquals( new Move( "a1", "a2" ), move );
	}

	@Test //same test as previous but changing colours
	public void simplePositionTriangulateByColor() {
		Position position = new Position( Side.BLACK );
		position.add( Side.BLACK, "a1", PieceType.KING );
		position.add( Side.WHITE, "c1", PieceType.KING );

		Set< Move > moves = position.getNormalMoves();

		assertEquals( 1, moves.size() );
		final Move move = moves.iterator().next();
		assertEquals( new Move( "a1", "a2" ), move );
	}

	@Test
	public void simplePositionNoMoves() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "a1", PieceType.KING );

		position.add( Side.BLACK, "b1", PieceType.BISHOP ); //any?
		position.add( Side.BLACK, "a2", PieceType.PAWN ); //any?
		position.add( Side.BLACK, "b2", PieceType.PAWN ); //any?
		position.add( Side.BLACK, "c1", PieceType.KING );

		Set< Move > moves = position.getMoves();

		assertEquals( 0, moves.size() );
	}

	@Test( expected = IllegalStateException.class )
	public void cannotMoveFromTerminalPosition() {
		Position position = Position.getInitialPosition();
		position.move( Move.RESIGN ).move( new Move( "e7", "e5" ) );
	}

	@Test
	public void position() {
		Position.getInitialPosition().isTerminal();
	}

}
