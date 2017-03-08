package com.leokom.chess.player.legal.brain.normalized;

import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Ensure normalized evaluators correctly implement contract
 * [ 0, 1 ]
 *
 * Author: Leonid
 * Date-time: 27.08.16 19:44
 */
@RunWith( Parameterized.class )
public class NormalizedEvaluatorsCorrectnessTest {
	@Parameters
	public static Iterable<EvaluatorType> parameters() {
		return Arrays.asList( EvaluatorType.values() );
	}

	@Parameter
	public EvaluatorType evaluatorType;

	@Test
	public void allMovesInCorrectRangeFromInitialPosition() {
		assertAllMovesCorrectlyEvaluated( Position.getInitialPosition() );
	}

	@Test
	public void allCorrectInSomeSimplePosition() {
		final Position position = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.KING )
				.add( Side.WHITE, "b1", PieceType.QUEEN )
				.add( Side.BLACK, "h8", PieceType.KING )
				.setSide( Side.WHITE )
				.build();

		assertAllMovesCorrectlyEvaluated( position );
	}

	private void assertAllMovesCorrectlyEvaluated( Position position ) {
		position.getMoves().forEach(
			move -> {
				final double value = new NormalizedEvaluatorFactory().get( evaluatorType ).evaluateMove( position, move );
				assertTrue( String.format( "Evaluator %s returned wrong value %s for move %s", evaluatorType, value, move ),
					value >= 0.0 && value <= 1.0 );
			}
		);
	}
}
