package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;

import static junit.framework.Assert.assertTrue;

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

	private void assertAllMovesCorrectlyEvaluated( Position position ) {
		position.getMoves().forEach(
			move -> {
				final double value = new NormalizedEvaluatorFactory().get( evaluatorType ).evaluateMove( position, move );
				assertTrue( String.format( "Evaluator %s returned wrong value %s", evaluatorType, value ),
					value >= 0.0 && value <= 1.0 );
			}
		);
	}
}
