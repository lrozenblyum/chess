package com.leokom.chess;

import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import com.leokom.chess.player.legal.LegalPlayer;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluatorBuilder;
import org.apache.logging.log4j.LogManager;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Engine to run multiple games with some delta
 *
 * Author: Leonid
 * Date-time: 04.05.16 21:29
 */
@Ignore( "Till we find a way to exclude them nicely in IDEA" )
public class SimulatorMultiDeltaIT {

	/*
	 * Protection property.
	 * Coefficient :: [ 0 .. 90 ] delta 10 ==> 10 coefficients probed
	 * Each probe :: 10 games (5 pairs of games with colours switched)
	 *
	 */
	@Test
	public void protectionDeltas() {
		simulateDeltas( this::createProtector, 5 );
	}

	@Test
	public void attackingDeltas() {
		simulateDeltas( this::createAttacker, 1 );
	}

	private LegalPlayer createAttacker( Integer coefficient ) {
		return new LegalPlayer( new MasterEvaluatorBuilder().weight( EvaluatorType.ATTACK, coefficient ).build() );
	}

	private void simulateDeltas( Function< Integer, LegalPlayer > whitePlayerGenerator, int gamePairsPerIteration ) {
		Map< Integer, SimulatorStatistics > statisticsMap = new TreeMap<>();

		for ( int coefficient = 0; coefficient <= 90; coefficient += 10 ) {
			final LegalPlayer protectionBasedPlayer = whitePlayerGenerator.apply( coefficient );
			final LegalPlayer classicPlayer = new LegalPlayer();

			final SimulatorStatistics stats = new Simulator( protectionBasedPlayer, classicPlayer )
				.gamePairs( gamePairsPerIteration ).run();
			statisticsMap.put( coefficient, stats );
		}

		printResults( statisticsMap );
	}


	private LegalPlayer createProtector( int coefficient ) {
		return new LegalPlayer( new MasterEvaluatorBuilder().weight( EvaluatorType.PROTECTION, coefficient ).build() );
	}

	private void printResults( Map<Integer, SimulatorStatistics> statisticsMap ) {
		final String statsPrettyPrinted = statisticsMap.entrySet().stream()
				.map( entry -> entry.getKey() + " ==> " + entry.getValue() )
				.collect( Collectors.joining( "\n" ) );


		LogManager.getLogger().info( "STATISTICS :::\n {}", statsPrettyPrinted );
	}

	/*
	Protector
	1hour 53 minutes 55 seconds
	10 * 10 games
	0 ==> SimulatorStatistics{firstWins=1, secondWins=8, totalGames=10}
	10 ==> SimulatorStatistics{firstWins=8, secondWins=2, totalGames=10}
	20 ==> SimulatorStatistics{firstWins=5, secondWins=5, totalGames=10}
	30 ==> SimulatorStatistics{firstWins=5, secondWins=4, totalGames=10}
	40 ==> SimulatorStatistics{firstWins=1, secondWins=6, totalGames=10}
	50 ==> SimulatorStatistics{firstWins=3, secondWins=5, totalGames=10}
	60 ==> SimulatorStatistics{firstWins=3, secondWins=3, totalGames=10}
	70 ==> SimulatorStatistics{firstWins=5, secondWins=0, totalGames=10}
	80 ==> SimulatorStatistics{firstWins=5, secondWins=1, totalGames=10}
	90 ==> SimulatorStatistics{firstWins=4, secondWins=2, totalGames=10}
	 */

	/*
	Protector
	27 min 51 sec
	0 ==> SimulatorStatistics{firstWins=2, secondWins=8, totalGames=10}
	10 ==> SimulatorStatistics{firstWins=8, secondWins=2, totalGames=10}
	20 ==> SimulatorStatistics{firstWins=5, secondWins=4, totalGames=10}
	30 ==> SimulatorStatistics{firstWins=5, secondWins=3, totalGames=10}
	40 ==> SimulatorStatistics{firstWins=2, secondWins=4, totalGames=10}
	50 ==> SimulatorStatistics{firstWins=1, secondWins=5, totalGames=10}
	60 ==> SimulatorStatistics{firstWins=2, secondWins=3, totalGames=10}
	70 ==> SimulatorStatistics{firstWins=8, secondWins=2, totalGames=10}
	80 ==> SimulatorStatistics{firstWins=6, secondWins=0, totalGames=10}
	90 ==> SimulatorStatistics{firstWins=4, secondWins=1, totalGames=10}
	 */

	/* Attacker (with debug logging enabled)
	10 min 38 sec
	  10 * 2 games
	STATISTICS :::
		0 ==> SimulatorStatistics{firstWins=1, secondWins=0, totalGames=2}
		10 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		20 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		30 ==> SimulatorStatistics{firstWins=0, secondWins=1, totalGames=2}
		40 ==> SimulatorStatistics{firstWins=0, secondWins=1, totalGames=2}
		50 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		60 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		70 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		80 ==> SimulatorStatistics{firstWins=0, secondWins=0, totalGames=2}
		90 ==> SimulatorStatistics{firstWins=0, secondWins=1, totalGames=2}

	 */

}
