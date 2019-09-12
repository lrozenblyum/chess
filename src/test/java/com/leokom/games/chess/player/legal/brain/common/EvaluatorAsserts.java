package com.leokom.games.chess.player.legal.brain.common;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.PositionBuilder;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 15.07.14 21:42
 */
public class EvaluatorAsserts {
	private final GenericEvaluator<Position, Move> evaluator;

	public EvaluatorAsserts( GenericEvaluator<Position, Move>  evaluator ) {
		this.evaluator = evaluator;
	}

	public void assertFirstBetter( PositionBuilder position, Move expectedBetter, Move expectedWorse ) {
		if ( ! expectedBetter.isSpecial() ) {
			position.setSideOf( expectedBetter.getFrom() );
		}
		else if ( position.getSideToMove() == null ) {
			throw new IllegalArgumentException( "We are unable autodetect the side to move from a special move, you should have set up it in the builder" );
		}
		assertFirstBetter( position.build(), expectedBetter, expectedWorse );
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

		assertEquals(String.format("%s -> %s must be equal to %s -> %s ",
				firstMove, firstMoveEstimate, secondMove, secondMoveEstimate),
				firstMoveEstimate,
				secondMoveEstimate,
				0);
	}

	void assertEvaluation( double expected, Position position, Move move ) {
		assertEquals( expected, evaluator.evaluateMove( position, move ), 0 );
	}
}
