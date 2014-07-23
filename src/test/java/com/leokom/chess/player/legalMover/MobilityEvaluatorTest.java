package com.leokom.chess.player.legalMover;

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
		Position position = new Position();
		position.add( Side.WHITE, "a1", PieceType.QUEEN );

		Move expectedBetter = new Move( "a1", "d4" ); //high mobility
		Move expectedWorse = new Move( "a1", "h8" ); //low mobility

		new EvaluatorAsserts( evaluator ).assertFirstBetter( position, expectedBetter, expectedWorse );
	}
}
