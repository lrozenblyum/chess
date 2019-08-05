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
	public void leaveAttackedSquare() {
		Position position = new PositionBuilder()
				.add(Side.WHITE, "a1", PieceType.BISHOP)
				.add(Side.BLACK, "h1", PieceType.ROOK)
				.build();

		Move removingFromAttack = new Move( "a1", "b2" );
		Move keepingOnAttackLine = new Move( "a1", "h8" );

		asserts.assertFirstBetter( position, removingFromAttack, keepingOnAttackLine );
	}

	//just one more test (previously it was job of ProtectionEvaluator)
	@Test
	public void leaveAttackedSquare2() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "h8", PieceType.ROOK );
		position.add( Side.WHITE, "c2", PieceType.PAWN );

		position.add( Side.BLACK, "g6", PieceType.KNIGHT ); //attacks the rook

		Move leavingAttackedSquare = new Move( "h8", "b8" );

		Move stayingCalm = new Move( "c2", "c3" );

		asserts.assertFirstBetter( position, leavingAttackedSquare, stayingCalm );
	}

	@Test
	public void doubleAttackMeansNeedToAct() {
		Position position = new Position( Side.BLACK );
		//attacks b3, d3
		position.add( Side.WHITE, "c1", PieceType.KNIGHT );

		position.add( Side.BLACK, "b3", PieceType.PAWN );
		position.add( Side.BLACK, "d3", PieceType.PAWN );

		position.add( Side.BLACK, "g1", PieceType.KING );

		Move leaveOneOfAttacked = new Move( "b3", "b2" );
		Move ignoreAttack = new Move( "g1", "h1" );

		asserts.assertFirstBetter( position, leaveOneOfAttacked, ignoreAttack );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.ATTACK;
	}
}
