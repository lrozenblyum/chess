package com.leokom.chess;

/**
 * Author: Leonid
 * Date-time: 06.04.16 22:13
 */
final class SimulatorStatistics {
	private int firstWins;
	private int secondWins;

	SimulatorStatistics( int firstWins, int secondWins ) {
		this.firstWins = firstWins;
		this.secondWins = secondWins;
	}

	int getFirstWins() {
		return firstWins;
	}

	int getSecondWins() {
		return secondWins;
	}
}