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

	@Test
	public void unHardCodeD5() {
		Position position = new Position();
		position.add( Side.WHITE, "e4", PieceType.QUEEN );
		position.add( Side.BLACK, "e8", PieceType.PAWN );

		Move capture = new Move( "e4", "e8" );
		Move justMove = new Move( "e4", "d5" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, capture, justMove );
	}

	@Test
	public void shouldPromotionGiveMaterialBenefit() {
		Position position = new Position();
		position.add( Side.WHITE, "e7", PieceType.PAWN );
		position.add( Side.WHITE, "b2", PieceType.PAWN );

		Move promotion = new Move( "e7", "e8B" );
		Move justMove = new Move( "b2", "b3" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, promotion, justMove );

	}

	@Test
	public void shouldPromotionToHigherPieceGiveMaterialBenefit() {
		Position position = new Position();
		position.add( Side.WHITE, "e7", PieceType.PAWN );

		Move toBishop = new Move( "e7", "e8B" );
		Move toQueen = new Move( "e7", "e8Q" );

		new EvaluatorAsserts( evaluator )
				.assertFirstBetter( position, toQueen, toBishop );

	}
}
