package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

import java.util.HashMap;
import java.util.Map;

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
	private final Communicator communicator;
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
	WinboardCommanderImpl( Communicator communicator ) {
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
	public void anotherPlayerMoved( String move ) {
		this.communicator.send( "move " + move );
	}

	@Override
	public void offerDraw() {
		this.communicator.send( "offer draw" );
	}

	@Override
	public void resign() {
		this.communicator.send( "resign" );
	}


	@Override
	public void onProtover( ProtoverListener protoverListener ) {
		this.protoverListener = protoverListener;
	}

	@Override
	public void onGo( GoListener listener ) {
		this.goListener = listener;
	}

	@Override
	public void onUserMove( UserMoveListener listener ) {
		this.userMoveListener = listener;
	}

	@Override
	public void onOfferDraw( OfferDrawListener listener ) {
		this.offerDrawListener = listener;
	}

	@Override
	public void onXBoard( XBoardListener listener ) {
		this.xboardListener = listener;
	}


	//we could use adapters
	//but now I directly force the listeners to extend the interface
	private Map<String, NoParametersListener> listenersWithoutParams = new HashMap<String, NoParametersListener>();

	@Override
	public void onQuit( final QuitListener listener ) {
		this.listenersWithoutParams.put( "quit", listener );
	}

	@Override
	public void processInputFromServer() {
		String receivedCommand = communicator.receive();
		if ( receivedCommand.startsWith( "protover" ) && protoverListener != null ) {
			//TODO: validation??
			protoverListener.execute( Integer.parseInt(receivedCommand.split( " " )[ 1 ]) );
		}

		for ( String command : listenersWithoutParams.keySet() ) {
			if ( receivedCommand.equals( command ) ) {
				listenersWithoutParams.get( command ).execute();
			}
		}

		if ( receivedCommand.equals( "go" ) && goListener != null ) {
			goListener.execute();
		}

		if ( receivedCommand.startsWith( "usermove" ) && userMoveListener != null ) {
			userMoveListener.execute();
		}

		if ( receivedCommand.equals( "draw" ) && offerDrawListener != null ) {
			offerDrawListener.execute();
		}

		if ( receivedCommand.equals( "xboard" ) && xboardListener != null ) {
			xboardListener.execute();
		}
	}
}
