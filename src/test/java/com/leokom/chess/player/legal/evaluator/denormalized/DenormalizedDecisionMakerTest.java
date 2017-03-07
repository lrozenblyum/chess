package com.leokom.chess.player.legal.evaluator.denormalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:58
 */
public class DenormalizedDecisionMakerTest {
	private DecisionMaker decisionMaker;

	@Before
	public void prepare() {
		decisionMaker = new DenormalizedDecisionMaker();
	}

	@Test
	public void canFindMoves() {
		assertFalse( decisionMaker.findBestMove( Position.getInitialPosition() ).isEmpty() );
	}

	/**
	 * Avoid really 'stupid' moves
	 */
	@Test
	public void beSmartALittle() {
		PositionBuilder position =
			new PositionBuilder()
				.add( Side.WHITE, "a2", PieceType.PAWN )
				.add( Side.WHITE, "b2", PieceType.PAWN )
				.add( Side.WHITE, "a1", PieceType.QUEEN )
				.add( Side.BLACK, "c1", PieceType.ROOK )
				.add( Side.WHITE, "h6", PieceType.KING )
				.add( Side.BLACK, "h8", PieceType.KING )
				.setSide( Side.WHITE );

		//otherwise we'll lose the queen
		Move captureIsSmartest = new Move( "a1", "c1" );

		final List< Move > movesFound = decisionMaker.findBestMove( position.build() );
		assertFalse( movesFound.isEmpty() );
		assertEquals( captureIsSmartest, movesFound.get( 0 ) );
	}
}