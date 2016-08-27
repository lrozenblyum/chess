package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 03.08.15 22:15
 */
public class SpecialMoveEvaluatorTest {
	private Evaluator evaluator;

	@Before
	public void prepare() {
		evaluator = new SpecialMoveEvaluator();
	}

	@Test
	public void offerDrawToPrefer() {
		new EvaluatorAsserts( evaluator ).assertFirstBetter( Position.getInitialPosition(), Move.OFFER_DRAW, Move.RESIGN );
	}
}
