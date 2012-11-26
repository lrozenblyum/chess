package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Middle-level command work for Winboard.
 * It must know about commands format.
 * Probably it mustn't know about the commands sequence
 * (but this will be clarified by TDD)
 *
 *
 * When we'll need it for test purposes - we'll extract interface to test more
 * high-level component.
 *
 * Author: Leonid
 * Date-time: 10.11.12 21:22
 */
class WinboardCommanderImpl implements WinboardCommander {
	private Communicator communicator;
	private ProtoverListener protoverListener;
	private QuitListener quitListener;
	private GoListener goListener;
	private UserMoveListener userMoveListener;
	private OfferDrawListener offerDrawListener;
	private XBoardListener xboardListener;

	/**
	 * Create the commander, with communicator injected
	 *
	 * @param communicator low-level framework to use to send/receive the commands
	 */
	public WinboardCommanderImpl( Communicator communicator ) {
		this.communicator = communicator;
	}

	/**
	 * Switches Winboard engine in 'features set up mode'
	 */
	@Override
	public void startInit() {
		communicator.send( "feature done=0" );
	}

	@Override
	public void enableUserMovePrefixes() {
		communicator.send( "feature usermove=1" );
	}

	@Override
	public void finishInit() {
		communicator.send( "feature done=1" );
	}

	@Override
	public void agreeToDrawOffer() {
		//'draw': The engine's opponent offers the engine a draw.
		// To accept the draw, send "offer draw".

		//TODO: need implement some method ignoreDrawOffer?
		// To decline, ignore the offer (that is, send nothing).
		communicator.send( "offer draw" );
	}

	@Override
	public void setProtoverListener( ProtoverListener protoverListener ) {
		this.protoverListener = protoverListener;
	}

	@Override
	public void setQuitListener( QuitListener listener ) {
		this.quitListener = listener;
	}

	@Override
	public void setGoListener( GoListener listener ) {
		this.goListener = listener;
	}

	@Override
	public void setUserMoveListener( UserMoveListener listener ) {
		this.userMoveListener = listener;
	}

	@Override
	public void setOfferDrawListener( OfferDrawListener listener ) {
		this.offerDrawListener = listener;
	}

	@Override
	public void setXboardListener( XBoardListener listener ) {
		this.xboardListener = listener;
	}

	@Override
	public void anotherPlayerMoved( String move ) {
		this.communicator.send( "move " + move );
	}

	@Override
	public void offerDraw() {
		this.communicator.send( "offer draw" );
	}

	@Override
	public void getInput() {
		String whatToReceive = communicator.receive();
		if ( whatToReceive.startsWith( "protover" ) && protoverListener != null ) {
			//TODO: validation??
			protoverListener.execute( Integer.parseInt(whatToReceive.split( " " )[ 1 ]) );
		}
		if ( whatToReceive.equals( "quit" ) && quitListener != null ) {
			quitListener.execute();
		}

		if ( whatToReceive.equals( "go" ) && goListener != null ) {
			goListener.execute();
		}

		//TODO: not fully test-covered
		if ( whatToReceive.startsWith( "usermove" ) && userMoveListener != null ) {
			userMoveListener.execute();
		}

		if ( whatToReceive.equals( "draw" ) && offerDrawListener != null ) {
			offerDrawListener.execute();
		}

		if ( whatToReceive.equals( "xboard" ) && xboardListener != null ) {
			xboardListener.execute();
		}
	}

	@Override
	public Communicator getCommunicator() {
		return this.communicator;
	}
}
