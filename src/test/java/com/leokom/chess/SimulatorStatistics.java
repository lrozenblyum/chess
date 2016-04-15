package com.leokom.chess;

/**
 * Author: Leonid
 * Date-time: 06.04.16 22:13
 */
final class SimulatorStatistics {
	private long firstWins;
	private long secondWins;

	SimulatorStatistics( long firstWins, long secondWins ) {
		this.firstWins = firstWins;
		this.secondWins = secondWins;
	}

	long getFirstWins() {
		return firstWins;
	}

	long getSecondWins() {
		return secondWins;
	}
}