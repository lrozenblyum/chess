package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 24.07.14 22:39
 */
public class MaterialEvaluatorTest {

	private Evaluator evaluator;
	@Before
	public void prepare(){
		evaluator = new MaterialEvaluator();
	}

	@Test
	public void shouldCaptureBetter(){
		Position position = new Position();
		position.add( Side.WHITE, "e4", PieceType.PAWN );
		position.add( Side.BLACK, "d5", PieceType.PAWN );

		Move capture = new Move( "e4", "d5" );
		Move justMove = new Move( "e4", "e5" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, capture, justMove );
	}

}
