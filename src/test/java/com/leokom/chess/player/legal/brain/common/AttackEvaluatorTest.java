package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.*;
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

	@Test
	public void removingFromOpponentAttackIsBetterThanNot() {
		Position position = new PositionBuilder()
				.add(Side.WHITE, "a1", PieceType.BISHOP)
				.add(Side.BLACK, "h1", PieceType.ROOK)
				.build();

		Move removingFromAttack = new Move( "a1", "b2" );
		Move keepingOnAttackLine = new Move( "a1", "h8" );

		asserts.assertFirstBetter( position, removingFromAttack, keepingOnAttackLine );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.ATTACK;
	}
}
