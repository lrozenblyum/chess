package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 23.07.14 21:44
 * This test is generic (verifies both normalized and denormalized behavior)
 */
public class MobilityEvaluatorTest extends EvaluatorTestCase {

	@Test
	public void shouldQueenControlMore() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "a1", PieceType.QUEEN );
		position.add( Side.BLACK, "h7", PieceType.QUEEN );

		Move expectedBetter = new Move( "a1", "d4" ); //high mobility
		Move expectedWorse = new Move( "a1", "h8" ); //low mobility

		asserts.assertFirstBetter( position, expectedBetter, expectedWorse );
	}

	@Test
	public void shouldKingHaveMoreFreedom() {
		Position position = new Position( Side.BLACK );
		position.add( Side.BLACK, "f8", PieceType.KING );

		position.add( Side.WHITE, "e8", PieceType.KNIGHT );
		position.add( Side.WHITE, "f7", PieceType.QUEEN );
		position.add( Side.WHITE, "e7", PieceType.PAWN );

		Move expectedBetter = new Move( "f8", "g7" ); //can move to f8, g8, h8, h7, g6, f6
		Move expectedWorse = new Move( "f8", "g8" ); //can move to f8, g7, h8, h7

		asserts.assertFirstBetter( position, expectedBetter, expectedWorse );
	}

	//0.5 attempt is to make analyze not only ours but the opponent's position
	@Test
	public void blockingOpponentMobilityIsBetterThanNot() {
		Position position = new Position( Side.WHITE );
		position.add( Side.BLACK, "a8", PieceType.QUEEN );
		position.add( Side.WHITE, "c8", PieceType.ROOK );

		//difference for the opponent, our mobility is not affected
		Move lessFreedomForQueen = new Move( "c8", "b8" );
		Move moreFreedomForQueen = new Move( "c8", "d8" );

		asserts.assertFirstBetter( position, lessFreedomForQueen, moreFreedomForQueen );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.MOBILITY;
	}
}
