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
	public void protectingBetterThanNot() {
		Position position = new Position();
		position.add( Side.WHITE, "h8", PieceType.ROOK );
		position.add( Side.WHITE, "c1", PieceType.PAWN );

		Move protectingPawn = new Move( "h8", "c8" );

		Move notProtectingPawn = new Move( "h8", "h7" );

		asserts.assertFirstBetter( position, protectingPawn, notProtectingPawn );
	}

	//backlog

	// 1) protecting a piece by another piece better than not
	// (even if no attack?
	// Maybe fact of attack should increase value of protective moves?)

	// 2) Protecting a more valuable piece is more important than less valuable
	// 3) Protecting BY less valuable piece is better than BY more valuable
	// 4) Double protection is better than single

}