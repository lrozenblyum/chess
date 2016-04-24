package com.leokom.chess;

import java.util.Objects;

/**
 * Author: Leonid
 * Date-time: 06.04.16 22:13
 */
final class SimulatorStatistics {
	private long firstWins;
	private long secondWins;
	private long totalGames;

	SimulatorStatistics( long totalGames, long firstWins, long secondWins ) {
		this.totalGames = totalGames;
		this.firstWins = firstWins;
		this.secondWins = secondWins;
	}

	long getFirstWins() {
		return firstWins;
	}

	long getSecondWins() {
		return secondWins;
	}
	SimulatorStatistics plus( SimulatorStatistics another ) {
		return new SimulatorStatistics(
				totalGames + another.totalGames,
				firstWins + another.firstWins,
				secondWins + another.secondWins );
	}

	@Override
	public boolean equals( Object o ) {
		if ( this == o ) {
			return true;
		}
		if ( o == null || getClass() != o.getClass() ) {
			return false;
		}
		SimulatorStatistics that = ( SimulatorStatistics ) o;
		return firstWins == that.firstWins &&
				secondWins == that.secondWins &&
				totalGames == that.totalGames;
	}

	@Override
	public int hashCode() {
		return Objects.hash( firstWins, secondWins, totalGames );
	}

	@Override
	public String toString() {
		return "SimulatorStatistics{" +
				"firstWins=" + firstWins +
				", secondWins=" + secondWins +
				", totalGames=" + totalGames +
				'}';
	}

	long getTotalGames() {
		return totalGames;
	}

	static SimulatorStatistics EMPTY() {
		return new SimulatorStatistics( 0, 0, 0 );
	}
}