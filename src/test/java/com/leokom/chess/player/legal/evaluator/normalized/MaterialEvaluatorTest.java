package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorAsserts;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 24.07.14 22:39
 */
public class MaterialEvaluatorTest {

	private EvaluatorAsserts asserts;
	@Before
	public void prepare(){
		Evaluator evaluator = new NormalizedEvaluatorFactory().get( EvaluatorType.MATERIAL );

		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void shouldCaptureBetter(){
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e4", PieceType.PAWN );
		position.add( Side.BLACK, "d5", PieceType.PAWN );

		Move capture = new Move( "e4", "d5" );
		Move justMove = new Move( "e4", "e5" );

		asserts.assertFirstBetter( position, capture, justMove );
	}

	@Test
	public void unHardCodeD5() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e4", PieceType.QUEEN );
		position.add( Side.BLACK, "e8", PieceType.PAWN );

		Move capture = new Move( "e4", "e8" );
		Move justMove = new Move( "e4", "d5" );

		asserts.assertFirstBetter( position, capture, justMove );
	}

	@Test
	public void shouldPromotionGiveMaterialBenefit() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e7", PieceType.PAWN );
		position.add( Side.WHITE, "b2", PieceType.PAWN );

		Move promotion = new Move( "e7", "e8B" );
		Move justMove = new Move( "b2", "b3" );

		asserts.assertFirstBetter( position, promotion, justMove );

	}

	@Test
	public void shouldPromotionToHigherPieceGiveMaterialBenefit() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "e7", PieceType.PAWN );

		Move toBishop = new Move( "e7", "e8B" );
		Move toQueen = new Move( "e7", "e8Q" );

		asserts.assertFirstBetter( position, toQueen, toBishop );
	}
}
