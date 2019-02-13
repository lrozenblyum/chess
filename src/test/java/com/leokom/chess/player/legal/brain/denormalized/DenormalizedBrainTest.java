package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.Brain;
import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:58
 */
public class DenormalizedBrainTest {
	private Brain brain;

	@Before
	public void prepare() {
		brain = new DenormalizedBrain();
	}

	@Test
	public void canFindMoves() {
		assertFalse( brain.findBestMove( Position.getInitialPosition() ).isEmpty() );
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

		final List< Move > movesFound = brain.findBestMove( position.build() );
		assertFalse( movesFound.isEmpty() );
		assertEquals( captureIsSmartest, movesFound.get( 0 ) );
	}

	@Test
	public void nonStandardEvaluatorWeightsMustNotCrash() {
		//maximal legal 1.0 weight set up
		Map<EvaluatorType, Double> weights = Arrays.stream(EvaluatorType.values()).collect(Collectors.toMap(Function.identity(), type -> 1.0));
		EvaluatorWeights evaluatorWeights = new EvaluatorWeights(weights);
		assertNotNull( new DenormalizedBrain( evaluatorWeights ).findBestMove( Position.getInitialPosition() ) );
	}
}