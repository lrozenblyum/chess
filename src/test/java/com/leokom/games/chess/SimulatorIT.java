package com.leokom.games.chess;

import com.leokom.games.chess.player.legal.LegalPlayer;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluatorBuilder;
import com.leokom.games.chess.player.legal.brain.normalized.NormalizedBrain;
import com.leokom.games.chess.player.legal.brain.random.RandomBrain;
import com.leokom.games.chess.player.legal.brain.simple.SimpleBrain;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
@Ignore( "Till we find a way to exclude them nicely in IDEA" )
public class SimulatorIT {
	//we expect the normalized brain of the legal player is much smarter than the simple one
	@Test
	public void legalVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator( new LegalPlayer( new NormalizedBrain( new MasterEvaluator() ) ), new LegalPlayer( new SimpleBrain() ) ).run();

		assertEquals( new SimulatorStatistics( 2, 2, 0 ), statistics );
	}

	@Test
	public void simpleVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator( new LegalPlayer( new SimpleBrain() ), new LegalPlayer( new SimpleBrain() ) ).run();

		//now simple vs simple correctly draws at the second move
		assertEquals( new SimulatorStatistics( 2, 0, 0 ), statistics );
	}

	@Test
	public void randomVsRandom() {
		final SimulatorStatistics statistics = new Simulator(
			new LegalPlayer( new RandomBrain() ),
			new LegalPlayer( new RandomBrain() )
		).run();

		assertNotNull( statistics );
	}

	@Test
	public void simpleVsRandom() {
		final SimulatorStatistics statistics = new Simulator(
				new LegalPlayer( new SimpleBrain() ),
				new LegalPlayer( new RandomBrain() )
		).run();

		assertNotNull(statistics);
	}

	@Test
	public void normalizedDepth1VsRandom() {
		final SimulatorStatistics statistics = new Simulator(
				new LegalPlayer( new NormalizedBrain( new MasterEvaluator(), 1 ) ),
				new LegalPlayer( new RandomBrain() )
		).run();

		assertNotNull( statistics );
	}

	@Test
	public void normalizedDepth2VsRandom() {
		final SimulatorStatistics statistics = new Simulator(
				new LegalPlayer( new NormalizedBrain( new MasterEvaluator(), 2 ) ),
				new LegalPlayer( new RandomBrain() )
		).run();

		//we still cannot assert anything smarter. Random player has a theoretical possibility to win a grandmaster.
		assertNotNull( statistics );
	}

	@Test
	public void denormalizedVsRandom() {
		final SimulatorStatistics statistics = new Simulator(
				new LegalPlayer( new DenormalizedBrain() ),
				new LegalPlayer( new RandomBrain() )
		).run();

		assertNotNull( statistics );
	}

	//non-deterministic, it's not a business-requirement
	@Test
	public void legalVsLegalCustomEvaluator() {
		final Evaluator brainLikesToEatPieces = new MasterEvaluatorBuilder().weight( EvaluatorType.MATERIAL, 1.0 ).build();

		final SimulatorStatistics statistics =
			new Simulator( new LegalPlayer(), new LegalPlayer( brainLikesToEatPieces ) ).run();

		//who eats - that one wins
		assertEquals( new SimulatorStatistics( 2, 0, 2 ), statistics );
	}

	//non-deterministic, it's not a business-requirement
	@Test
	public void legalPlayerEqualProbableDraw() {
		final SimulatorStatistics statistics = new Simulator( new LegalPlayer(), new LegalPlayer() ).run();

		assertEquals( new SimulatorStatistics( 2, 1, 1 ), statistics );
	}

	//expected : new skills are better
	@Test
	public void newBrainShouldBeBetter() {
		final LegalPlayer withNewSkills = new LegalPlayer( new DenormalizedBrain() );
		final LegalPlayer classicPlayer = new LegalPlayer( new NormalizedBrain( new MasterEvaluator() ) );
		final SimulatorStatistics statistics = new Simulator( withNewSkills, classicPlayer )
				.gamePairs( 5 )
				.run();

		assertTrue( statistics + " should prove advantage of the first player",
			statistics.getFirstWins() > statistics.getSecondWins() );
	}

	@Test
	public void normalizedPlayerWithDepth2IsBetterThanDepth1() {
		final LegalPlayer deeperThinker = new LegalPlayer( new NormalizedBrain( new MasterEvaluator(), 2 ) );
		final LegalPlayer classicPlayer = new LegalPlayer( new NormalizedBrain( new MasterEvaluator(), 1 ) );
		final SimulatorStatistics statistics = new Simulator( deeperThinker, classicPlayer )
				.gamePairs( 5 )
				.run();

		assertTrue( statistics + " should prove advantage of the first player",
				statistics.getFirstWins() > statistics.getSecondWins() );
	}
}