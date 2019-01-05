package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import org.junit.Assert;

/**
 * Author: Leonid
 * Date-time: 15.07.14 21:42
 */
public class EvaluatorAsserts {
	private final Evaluator evaluator;

	public EvaluatorAsserts( Evaluator evaluator ) {
		this.evaluator = evaluator;
	}

	public void assertFirstBetter( PositionBuilder position, Move expectedBetter, Move expectedWorse ) {
		assertFirstBetter( position.setSideOf( expectedBetter.getFrom() ).build(), expectedBetter, expectedWorse );
	}

	public void assertFirstBetter( Position position, Move expectedBetter, Move expectedWorse ) {
		double betterMoveEstimate = evaluator.evaluateMove( position, expectedBetter );
		double worseMoveEstimate = evaluator.evaluateMove( position, expectedWorse );

		Assert.assertTrue(
				String.format( "%s -> %s must be better than %s -> %s ",
						expectedBetter, betterMoveEstimate, expectedWorse, worseMoveEstimate ),
				betterMoveEstimate > worseMoveEstimate );
	}

	void assertNoDifference( Position position, Move firstMove, Move secondMove ) {
		double firstMoveEstimate = evaluator.evaluateMove( position, firstMove );
		double secondMoveEstimate = evaluator.evaluateMove( position, secondMove );

		Assert.assertEquals(String.format("%s -> %s must be equal to %s -> %s ",
				firstMove, firstMoveEstimate, secondMove, secondMoveEstimate),
				firstMoveEstimate,
				secondMoveEstimate,
				0);
	}
}
