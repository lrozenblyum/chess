package com.leokom.games.chess.player.legal.brain.common;

import com.leokom.games.chess.engine.*;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 01.03.15 22:31
 */
public class TerminalEvaluatorTest extends EvaluatorTestCase {
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
	public void checkmateIsBetterThanObligatoryDraw() {
		PositionBuilder builder = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.ROOK )
				.add( Side.WHITE, "b7", PieceType.ROOK )
				.add( Side.BLACK, "h8", PieceType.KING )
				.rules( new RulesBuilder().movesTillDraw( 1 ).build() )
				.pliesCount( 1 );

		Move checkmateMove = new Move( "a1", "a8" );
		Move obligatoryDrawMove = new Move( "a1", "a2" );

		asserts.assertFirstBetter( builder, checkmateMove, obligatoryDrawMove );
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

	@Test
	public void drawClaimIsWorseThanCheckmate() {
		PositionBuilder builder = new PositionBuilder()
				.add( Side.WHITE, "c1", PieceType.ROOK )
				.add( Side.WHITE, "b7", PieceType.ROOK )
				.add( Side.BLACK, "h8", PieceType.KING );


		Move checkmateMove = new Move( "c1", "c8" );
		Move claimDrawMove = Move.CLAIM_DRAW;

		asserts.assertFirstBetter( builder, checkmateMove, claimDrawMove );
	}

	@Test
	public void drawClaimIsBetterThanResign() {
		PositionBuilder builder = new PositionBuilder()
				.add( Side.WHITE, "c1", PieceType.ROOK )
				.add( Side.WHITE, "b7", PieceType.ROOK )
				.add( Side.BLACK, "h8", PieceType.KING );


		asserts.assertFirstBetter( builder, Move.CLAIM_DRAW, Move.RESIGN );
	}

	@Override
	EvaluatorType getEvaluatorType() {
		return EvaluatorType.TERMINAL;
	}
}
