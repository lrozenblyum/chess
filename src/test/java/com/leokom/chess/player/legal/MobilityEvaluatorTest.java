package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 23.07.14 21:44
 */
public class MobilityEvaluatorTest {
	private Evaluator evaluator;

	@Before
	public void prepare() {
		evaluator = new MobilityEvaluator();
	}

	@Test
	public void shouldQueenControlMore() {
		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "a1", PieceType.QUEEN );
		position.add( Side.BLACK, "h7", PieceType.QUEEN );

		Move expectedBetter = new Move( "a1", "d4" ); //high mobility
		Move expectedWorse = new Move( "a1", "h8" ); //low mobility

		new EvaluatorAsserts( evaluator ).assertFirstBetter( position, expectedBetter, expectedWorse );
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

		new EvaluatorAsserts( evaluator ).assertFirstBetter( position, expectedBetter, expectedWorse );
	}
}
