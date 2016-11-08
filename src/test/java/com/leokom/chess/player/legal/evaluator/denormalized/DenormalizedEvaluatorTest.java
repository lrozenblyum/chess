package com.leokom.chess.player.legal.evaluator.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test specific denormalized evaluator features
 *
 * Author: Leonid
 * Date-time: 30.08.16 19:32
 */
public class DenormalizedEvaluatorTest {
	@Test
	public void specialMoveInfinity() {
		final double estimate = new DenormalizedEvaluatorFactory()
				.get( EvaluatorType.ATTACK ).evaluateMove( Position.getInitialPosition(), Move.RESIGN );

		//delta is ignored
		assertEquals( Double.NEGATIVE_INFINITY, estimate, 0 );
	}
}
