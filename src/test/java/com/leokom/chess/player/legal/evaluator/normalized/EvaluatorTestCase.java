package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.player.legal.evaluator.common.EvaluatorAsserts;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Before;

/**
 * Author: Leonid
 * Date-time: 27.08.16 15:23
 */
/* Must be public due to JUnit4 limitation : @Before method must be in a public class */
public abstract class EvaluatorTestCase {
	protected EvaluatorAsserts asserts;

	abstract EvaluatorType getEvaluatorType();

	@Before
	public final void prepareMe() {
		asserts = new EvaluatorAsserts( new NormalizedEvaluatorFactory().get( getEvaluatorType() ) );
	}
}
