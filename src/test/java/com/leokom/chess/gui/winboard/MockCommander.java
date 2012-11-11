package com.leokom.chess.gui.winboard;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:56
 */
public class MockCommander implements WinboardCommander {
	private int startInitCallsCount = 0;
	@Override
	public void startInit() {
		startInitCallsCount++;
	}

	@Override
	public void enableUserMovePrefixes() {

	}

	@Override
	public void finishInit() {

	}

	public int getStartInitCallsCount() {
		return startInitCallsCount;
	}
}
