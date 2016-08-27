package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorAsserts;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Before;
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
