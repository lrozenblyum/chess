package com.leokom.chess;

import java.util.Objects;

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

	@Override
	public boolean equals( Object another ) {
		if ( this == another ) {
			return true;
		}
		if ( another == null || getClass() != another.getClass() ) {
			return false;
		}

		SimulatorStatistics that = ( SimulatorStatistics ) another;
		return firstWins == that.firstWins &&
				secondWins == that.secondWins;
	}

	@Override
	public int hashCode() {
		return Objects.hash( firstWins, secondWins );
	}

	@Override
	public String toString() {
		return "SimulatorStatistics{" +
				"firstWins=" + firstWins +
				", secondWins=" + secondWins +
				'}';
	}
}