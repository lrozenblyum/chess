package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

public class CenterControlEvaluatorTest {
	private CenterControlEvaluator evaluator;
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		evaluator = new CenterControlEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void evaluateMove() {
		Position position = new Position( null );

		Move centerControlMove = new Move( null, null );
		Move notCenterControlMove = new Move( null, null );

		asserts.assertBetterMoveDetected( position, centerControlMove, notCenterControlMove );
	}
}