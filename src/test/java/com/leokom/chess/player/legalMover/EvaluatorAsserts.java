package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 15.07.14 21:42
 */
public class EvaluatorAsserts {
	private final Evaluator evaluator;

	public EvaluatorAsserts( Evaluator evaluator ) {
		this.evaluator = evaluator;
	}
	void assertFirstBetter( Position position, Move expectedBetter, Move expectedWorse ) {
		double betterMoveEstimate = evaluator.evaluateMove( position, expectedBetter );
		double worseMoveEstimate = evaluator.evaluateMove( position, expectedWorse );

		assertTrue(
				String.format( "Better move must have higher rating. Expected better : %s -> %s, expected worse: %s -> %s ",
						expectedBetter, betterMoveEstimate, expectedWorse, worseMoveEstimate ),
				betterMoveEstimate > worseMoveEstimate );
	}
}
