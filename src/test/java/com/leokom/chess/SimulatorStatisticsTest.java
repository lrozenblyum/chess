package com.leokom.chess;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Leonid
 * Date-time: 24.04.16 22:56
 */
public class SimulatorStatisticsTest {
	@Test
	public void addition() {
		SimulatorStatistics first = new SimulatorStatistics( 21, 1, 20 );
		SimulatorStatistics second = new SimulatorStatistics( 57, 21, 33 );
		assertEquals( new SimulatorStatistics( 78, 22, 53 ), first.plus( second ) );
	}
}