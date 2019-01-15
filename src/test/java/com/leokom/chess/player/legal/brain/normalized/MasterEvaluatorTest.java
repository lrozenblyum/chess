package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorAsserts;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MasterEvaluatorTest {
	private Evaluator evaluator;

	@Before
	public void prepare(){
		evaluator = new MasterEvaluator();
	}

	/**
	 * Avoid really 'stupid' moves
	 */
	@Test
	public void beSmartALittle() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g6", PieceType.QUEEN );
		position.add( Side.BLACK, "g7", PieceType.PAWN );
		//protects the pawn
		position.add( Side.BLACK, "g8", PieceType.ROOK );

		Move captureWithRiskToLoseQueen = new Move( "g6", "g7" );
		Move simpleMove = new Move( "g6", "g5" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, simpleMove, captureWithRiskToLoseQueen );
	}

	@Test
	public void resignIsWeak() {
		Position position = Position.getInitialPosition();

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, new Move( "e2", "e4" ), Move.RESIGN );
	}

	//enforce rules that are valid for the whole normalized package, to MasterEvaluator itself
	@Test
	public void allMovesMustBeEvaluatedFrom0To1() {
		Position position = Position.getInitialPosition();

		position.getMoves().forEach( move -> {
			double result = evaluator.evaluateMove(position, move);
			assertTrue(
					String.format( "The move %s must be evaluated in range [0,1], actually: %s", move, result )
					,result >= 0.0 && result <= 1.0 );
		} );
	}

}