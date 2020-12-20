package com.leokom.games.chess;

import com.leokom.games.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Enable matches between players
 * Check evaluators to find out their strengths
 *
 * Put into test scope to avoid including into Chess jar
 * Maybe it's worth separate project
 *
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
class Simulator {
	private static final int GAMES_IN_SINGLE_ITERATION = 2;
	private final Player first;
	private final Player second;
	private final Logger logger;

	private int timesToRun = 1;

	Simulator( Player first, Player second ) {
		this.first = first;
		this.second = second;
		this.logger = LogManager.getLogger();
	}

	/**
	 * Run simulator
	 * By default the players will play
	 * 2 consecutive games switching colours.
	 *
	 * This can be controlled by {@link #gamePairs(int)}
	 *
	 * @return statistics about game results
	 */
	SimulatorStatistics run() {
		logger.info("Starting simulation for {} and {}", first.name(), second.name());
		List< Player > winners = new ArrayList<>();
		IntStream.rangeClosed( 1, timesToRun ).forEach( iteration -> {
			logger.info( "Simulation # {} of {}: starting...", iteration, timesToRun );

			collectWinnerInfo(winners, createGame(first, second));
			collectWinnerInfo(winners, createGame(second, first));
			logger.info( "Simulation # {} of {}: done", iteration, timesToRun );
		} );

		final long firstWins = countWinsOf( winners, first );
		final long secondWins = countWinsOf( winners, second );
		final long totalGames = timesToRun * GAMES_IN_SINGLE_ITERATION;
		SimulatorStatistics simulatorStatistics = new SimulatorStatistics(totalGames, firstWins, secondWins);
		logger.info("The simulation has been finished. Stats: {}", simulatorStatistics);
		return simulatorStatistics;
	}

	private void collectWinnerInfo(List<Player> winners, Game game) {
		game.runGame();
		game.winner().ifPresent(winners::add);
	}

	private static long countWinsOf( List< Player > winners, Player player ) {
		return winners.stream()
				.filter( Objects::nonNull )
				//default reference equality is fine
				.filter( player::equals )
				.count();
	}

	//https://code.google.com/archive/p/mockito/wikis/MockingObjectCreation.wiki
	//pattern1: using one-line methods for object creation
	Game createGame( Player white, Player black ) {
		return new Game( white, black );
	}

	/**
	 * Override default 1 pair games
	 * @param times gamePairs of games to run
	 * @return this
	 */
	Simulator gamePairs( int times ) {
		timesToRun = times;
		return this;
	}
}
