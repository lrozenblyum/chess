package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;


public class ProtectionEvaluatorTest {
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		ProtectionEvaluator evaluator = new ProtectionEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void leaveAttackedSquare() {
		Position position = new Position();
		position.add( Side.WHITE, "h8", PieceType.ROOK );
		position.add( Side.WHITE, "c2", PieceType.PAWN );

		position.add( Side.BLACK, "g6", PieceType.KNIGHT ); //attacks the rook

		Move leavingAttackedSquare = new Move( "h8", "b8" );

		Move stayingCalm = new Move( "c2", "c3" );

		asserts.assertFirstBetter( position, leavingAttackedSquare, stayingCalm );
	}

	@Test
	public void doubleAttackMeansNeedToAct() {
		Position position = new Position();
		//attacks b3, d3
		position.add( Side.WHITE, "c1", PieceType.KNIGHT );

		position.add( Side.BLACK, "b3", PieceType.PAWN );
		position.add( Side.BLACK, "d3", PieceType.PAWN );

		position.add( Side.BLACK, "g1", PieceType.KING );

		Move leaveOneOfAttacked = new Move( "b3", "b2" );
		Move ignoreAttack = new Move( "g1", "h1" );

		asserts.assertFirstBetter( position, leaveOneOfAttacked, ignoreAttack );
	}


	//backlog


	// 1) protecting a piece by another piece better than not
	// (even if no attack?
	// Maybe fact of attack should increase value of protective moves?)

	// 2) Protecting a more valuable piece is more important than less valuable
	// 3) Protecting BY less valuable piece is better than BY more valuable
	// 4) Double protection is better than single


	// 5) Protecting is backwards (against capture)
	// and blocking (prevent attack by crossing)
	//difference?
}