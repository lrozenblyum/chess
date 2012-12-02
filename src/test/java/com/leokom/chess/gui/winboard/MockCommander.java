package com.leokom.chess.gui.winboard;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:56
 */
public class MockCommander implements WinboardCommander {
	private int finishInitCallsCount = 0;
	private int enableUserMovePrefixesCount = 0;
	@Override
	public void startInit() {
	}

	@Override
	public void enableUserMovePrefixes() {
		enableUserMovePrefixesCount++;
	}

	@Override
	public void finishInit() {
		finishInitCallsCount++;
	}

	@Override
	public void agreeToDrawOffer() {
	}

	@Override
	public void setProtoverListener( ProtoverListener protoverListener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setQuitListener( QuitListener listener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void getInput() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setGoListener( GoListener listener ) {
	}

	@Override
	public void setUserMoveListener( UserMoveListener listener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setOfferDrawListener( OfferDrawListener listener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void setXboardListener( XBoardListener listener ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void anotherPlayerMoved( String move ) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void offerDraw() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void resign() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public int getFinishInitCallsCount() {
		return finishInitCallsCount;
	}

	public int getEnableUserMovePrefixesCount() {
		return enableUserMovePrefixesCount;
	}
}
