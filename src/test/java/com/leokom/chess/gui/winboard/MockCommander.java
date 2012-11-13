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

	@Override
	public void agreeToDrawOffer() {
	}

	@Override
	public void setProtoverListener( ProtoverListener protoverListener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void getInput() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public int getStartInitCallsCount() {
		return startInitCallsCount;
	}
}
