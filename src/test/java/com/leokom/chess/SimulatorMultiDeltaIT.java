package com.leokom.chess;

import com.leokom.chess.player.legalMover.EvaluatorType;
import com.leokom.chess.player.legalMover.LegalPlayer;
import com.leokom.chess.player.legalMover.MasterEvaluatorBuilder;
import org.apache.logging.log4j.LogManager;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;


/**
 * Engine to run multiple games with some delta
 *
 * Author: Leonid
 * Date-time: 04.05.16 21:29
 */
public class SimulatorMultiDeltaIT {

	/*
	 * Protection property.
	 * Coefficient :: [ 0 .. 90 ] delta 10 ==> 10 coefficients probed
	 * Each probe :: 10 games (5 pairs of games with colours switched)
	 *
	 */
	@Test
	public void protectionDeltas() {
		Map< Integer, SimulatorStatistics > statisticsMap = new TreeMap<>();

		for ( int coefficient = 0; coefficient <= 90; coefficient += 10 ) {
			final LegalPlayer protectionBasedPlayer = new LegalPlayer( new MasterEvaluatorBuilder().weight( EvaluatorType.PROTECTION, coefficient ).build() );
			final LegalPlayer classicPlayer = new LegalPlayer();

			final SimulatorStatistics stats = new Simulator( protectionBasedPlayer, classicPlayer ).gamePairs( 5 ).run();
			statisticsMap.put( coefficient, stats );
		}

		printResults( statisticsMap );
	}

	private void printResults( Map<Integer, SimulatorStatistics> statisticsMap ) {
		final String statsPrettyPrinted = statisticsMap.entrySet().stream()
				.map( entry -> entry.getKey() + " ==> " + entry.getValue() )
				.collect( Collectors.joining( "\n" ) );


		LogManager.getLogger().info( "STATISTICS ::: {}", statsPrettyPrinted );
	}

	/*
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

}
