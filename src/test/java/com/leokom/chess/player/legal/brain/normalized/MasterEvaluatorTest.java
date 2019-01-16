package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.EvaluatorAsserts;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
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

		assertAllMovesEvaluatedIn0To1Range(position, evaluator);
	}

	private void assertAllMovesEvaluatedIn0To1Range(Position position, Evaluator evaluatorToValidate) {
		position.getMoves().forEach( move -> {
			double result = evaluatorToValidate.evaluateMove(position, move);
			assertTrue(
					String.format( "The move %s must be evaluated in range [0,1], actually: %s", move, result )
					,result >= 0.0 && result <= 1.0 );
		} );
	}

	@Test
	public void allMovesMustBeEvaluatedFrom0To1EvenWithCustomWeights() {
		Map<EvaluatorType, Double> weights = new HashMap<>();
		Arrays.stream( EvaluatorType.values() ).forEach( type -> weights.put( type, 1.0 ) );

		MasterEvaluator masterEvaluatorWithCustomWeigths = new MasterEvaluator(weights);
		assertAllMovesEvaluatedIn0To1Range( Position.getInitialPosition(), masterEvaluatorWithCustomWeigths );
	}

	//losing should get the minimal possible value
	@Test
	public void losingIsEvaluatedTo0() {
		Position position = Position.getInitialPosition();
		assertEquals( 0.0, evaluator.evaluateMove(position, Move.RESIGN), 0 );
	}

	@Test
	public void winningIsEvaluatedTo1() {
		Position position = Position.getInitialPosition()
				.move( "f2", "f3" )
				.move( "e7", "e5" )
				.move( "g2", "g4" );
		//fools checkmate is prepared


		Move checkmateMove = new Move( "d8", "h4" );
		assertEquals( 1, evaluator.evaluateMove( position, checkmateMove ), 0 );
	}

}