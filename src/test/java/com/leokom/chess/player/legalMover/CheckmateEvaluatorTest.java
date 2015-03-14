package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:31
 */
public class CheckmateEvaluatorTest {
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		CheckmateEvaluator evaluator = new CheckmateEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void checkmateBetterThanNot() {
		PositionBuilder builder = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.ROOK )
				.add( Side.WHITE, "b7", PieceType.ROOK )
				.add( Side.BLACK, "h8", PieceType.KING );


		Move checkmateMove = new Move( "a1", "a8" );
		Move notCheckmateMove = new Move( "a1", "a7" );

		asserts.assertFirstBetter( builder, checkmateMove, notCheckmateMove );

	}

	@Test
	public void resignIsWorseThanCheckmate() {
		PositionBuilder builder = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.ROOK )
				.add( Side.WHITE, "b7", PieceType.ROOK )
				.add( Side.BLACK, "h8", PieceType.KING );


		Move checkmateMove = new Move( "a1", "a8" );
		Move resignMove = Move.RESIGN;

		asserts.assertFirstBetter( builder, checkmateMove, resignMove );
	}
}
