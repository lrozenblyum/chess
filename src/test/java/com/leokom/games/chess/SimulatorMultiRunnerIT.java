package com.leokom.games.chess;

import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.games.chess.player.legal.LegalPlayer;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluatorBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Historical reasons of the collector usage: trying to avoid Travis limitation on test duration < 10 minutes:
 * run every game in a separate test, collect all results and analyze
 * Date-time: 24.04.16 22:54
 */
@Ignore( "Till we find a way to exclude them nicely in IDEA" )
@RunWith( Parameterized.class )
public class SimulatorMultiRunnerIT {
	private static final int COUNT_OF_PAIRS_OF_GAMES = 3;
	private static SimulatorStatistics collector = SimulatorStatistics.EMPTY();

	private final Logger logger = LogManager.getLogger();

	@AfterClass
	public static void afterAll() {
		//protector should win, shouldn't it?
		assertEquals( new SimulatorStatistics( COUNT_OF_PAIRS_OF_GAMES * 2, 0, 6 ), collector );
	}

	public SimulatorMultiRunnerIT( int index ) {
		logger.info( "Running test {}", index );
	}

	//REFACTOR: something like http://www.codeaffine.com/2013/04/10/running-junit-tests-repeatedly-without-loops/
	//would be a cleaner solution
	@Parameterized.Parameters
	public static Iterable<Object[]> data() {
		return IntStream.rangeClosed( 1, COUNT_OF_PAIRS_OF_GAMES ).mapToObj( index -> new Object[]{ index } )::iterator;
	}


	//first multiple simulation - big amount of GAMES
	//let's check whether Protection feature is important
	@Test
	public void runGamePair() {
		final Evaluator brainLikesToProtectItself = new MasterEvaluatorBuilder()
				.weight( EvaluatorType.PROTECTION, 1.0 ).build();

		final SimulatorStatistics statistics =
				new Simulator( new LegalPlayer(), new LegalPlayer( brainLikesToProtectItself ) )
					.run();

		collector = collector.plus( statistics );
	}
}
