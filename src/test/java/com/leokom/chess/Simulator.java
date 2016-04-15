package com.leokom.chess;

import com.leokom.chess.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
	private final Player first;
	private final Player second;

	Simulator( Player first, Player second ) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Run simulator
	 * By default the players will play
	 * 2 consecutive games switching colours.
	 *
	 * @return statistics about game results
	 */
	SimulatorStatistics run() {
		List< Player > winners = new ArrayList<>();
		winners.add( createGame( first, second ).run() );
		winners.add( createGame( second, first ).run() );

		final long firstWins = countWinsOf( winners, first );
		final long secondWins = countWinsOf( winners, second );

		return new SimulatorStatistics( firstWins, secondWins );
	}

	private long countWinsOf( List< Player > winners, Player player ) {
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
}
