package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import org.junit.Before;
import org.junit.Test;

public class CenterControlEvaluatorTest {
	private CenterControlEvaluator evaluator;
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		evaluator = new CenterControlEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void evaluateMove() {
		Position position = new Position( null );
		position.add( Side.WHITE, "c3", PieceType.KING );

		//controls d5, d4
		Move centerControlMove = new Move( "c3", "c4" );

		Move notCenterControlMove = new Move( "c3", "b2" );

		asserts.assertFirstBetter( position, centerControlMove, notCenterControlMove );
	}

	@Test
	public void blackKingToCenter() {
		Position position = new Position( null );
		position.add( Side.BLACK, "e7", PieceType.KING );

		//controls d5, e5
		Move centerControlMove = new Move( "e7", "e6" );

		Move notCenterControlMove = new Move( "e7", "d7" );

		asserts.assertFirstBetter( position, centerControlMove, notCenterControlMove );
	}

	@Test
	public void kingNearCenterBetterThanToBorder() {
		Position position = new Position( null );
		position.add( Side.BLACK, "b5", PieceType.KING );

		Move toCenter = new Move( "b5", "c5" );
		Move toBorder = new Move( "b5", "a5" );

		asserts.assertFirstBetter( position, toCenter, toBorder );
	}
}