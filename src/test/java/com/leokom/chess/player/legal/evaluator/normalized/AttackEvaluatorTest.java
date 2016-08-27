package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
public class AttackEvaluatorTest extends EvaluatorTestCase {
	@Test
	public void attackingIsBetter() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "h4", PieceType.KNIGHT );

		position.add( Side.BLACK, "h8", PieceType.ROOK );

		Move attacking = new Move( "h4", "g6" );

		Move notAttacking = new Move( "h4", "g2" );

		asserts.assertFirstBetter( position, attacking, notAttacking );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.ATTACK;
	}
}
