package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorAsserts;
import org.junit.Before;
import org.junit.Test;

public class MasterEvaluatorTest {
	private Evaluator evaluator;

	@Before
	public void prepare(){
		evaluator = new MasterEvaluator();
	}

	/**
	 * Avoid really 'stupid' moves
	 */
	@Test
	public void beSmartALittle() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "g6", PieceType.QUEEN );
		position.add( Side.BLACK, "g7", PieceType.PAWN );
		//protects the pawn
		position.add( Side.BLACK, "g8", PieceType.ROOK );

		Move captureWithRiskToLoseQueen = new Move( "g6", "g7" );
		Move simpleMove = new Move( "g6", "g5" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, simpleMove, captureWithRiskToLoseQueen );
	}
}