package com.leokom.chess;

import com.leokom.chess.player.Player;

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

	private int timesToRun = 1;

	Simulator( PlayerFactory.PlayerSelection first, PlayerFactory.PlayerSelection second ) {
		this( first.create(), second.create() );
	}

	Simulator( Player first, Player second ) {
		this.first = first;
		this.second = second;
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
		List< Player > winners = new ArrayList<>();
		IntStream.rangeClosed( 1, timesToRun ).forEach( iteration -> {
			winners.add( createGame( first, second ).run() );
			winners.add( createGame( second, first ).run() );
		} );

		final long firstWins = countWinsOf( winners, first );
		final long secondWins = countWinsOf( winners, second );
		final long totalGames = timesToRun * GAMES_IN_SINGLE_ITERATION;
		return new SimulatorStatistics( totalGames, firstWins, secondWins );
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
