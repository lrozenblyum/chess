package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;

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
		final Side winner = new Game( this.first, this.second ).run();
		final Side winner2 = new Game( this.second, this.first ).run();

		int firstWins = 0;
		int secondWins = 0;
		if ( winner == Side.WHITE ) {
			firstWins++;
		}
		else  if ( winner == Side.BLACK ) {
			secondWins++;
		}

		if ( winner2 == Side.WHITE ) {
			secondWins++;
		}
		else  if ( winner2 == Side.BLACK ) {
			firstWins++;
		}

		return new SimulatorStatistics( firstWins, secondWins );
	}
}
