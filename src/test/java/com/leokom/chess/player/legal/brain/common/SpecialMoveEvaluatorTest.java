package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 03.08.15 22:15
 */
public class SpecialMoveEvaluatorTest extends EvaluatorTestCase {
	@Test
	public void offerDrawToPrefer() {
		asserts.assertFirstBetter( Position.getInitialPosition(), Move.OFFER_DRAW, Move.RESIGN );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.SPECIAL_MOVE;
	}
}
