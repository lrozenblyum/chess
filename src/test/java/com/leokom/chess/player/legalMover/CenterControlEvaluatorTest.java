package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CenterControlEvaluatorTest {
	private CenterControlEvaluator evaluator;

	@Before
	public void prepare() {
		evaluator = new CenterControlEvaluator();
	}

	@Test
	public void evaluateMove() {
	Position position = new Position( null );

		Move centerControlMove = new Move( null, null );
		Move notCenterControlMove = new Move( null, null );

		assertBetterMoveDetected( position, centerControlMove, notCenterControlMove );
	}

	private void assertBetterMoveDetected( Position position, Move expectedBetter, Move expectedWorse ) {
		double betterMoveEstimate = evaluator.evaluateMove( position, expectedBetter );
		double worseMoveEstimate = evaluator.evaluateMove( position, expectedWorse );

		assertTrue(
			String.format( "Better move must have higher rating. Expected better : %s -> %s, expected worse: %s -> %s ",
				expectedBetter, betterMoveEstimate, expectedWorse, worseMoveEstimate ),
			betterMoveEstimate > worseMoveEstimate );
	}
}