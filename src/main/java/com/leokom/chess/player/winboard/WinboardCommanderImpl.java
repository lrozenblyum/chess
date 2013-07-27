package com.leokom.chess.player.winboard;

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

	//we could use adapters
	//but now I directly force the listeners to extend the interface
	private final Map<String, NoParametersListener> listenersWithoutParams = new HashMap<String, NoParametersListener>();
	private final Map<String, StringParameterListener> stringParameterListeners = new HashMap<String, StringParameterListener>();
	private final Map<String, IntParameterListener> intParameterListeners = new HashMap<String, IntParameterListener>();

	/**
	 * Create the commander, with communicator injected
	 *
	 * @param communicator low-level player to use to send/receive the commands
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
	public void opponentMoved( String move ) {
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
		intParameterListeners.put( "protover", protoverListener );
	}

	@Override
	public void onGo( GoListener listener ) {
		listenersWithoutParams.put( "go", listener );
	}

	@Override
	public void onUserMove( UserMoveListener listener ) {
		stringParameterListeners.put( "usermove", listener );
	}

	@Override
	public void onOfferDraw( OfferDrawListener listener ) {
		listenersWithoutParams.put( "draw", listener );
	}

	@Override
	public void onResign( ResignListener listener ) {
		//TODO: real hard-coding of Black resignation.
		listenersWithoutParams.put( "result 1-0 {Black resigns}", listener );
	}

	@Override
	public void onXBoard( XBoardListener listener ) {
		listenersWithoutParams.put( "xboard", listener );
	}

	@Override
	public void onQuit( final QuitListener listener ) {
		listenersWithoutParams.put( "quit", listener );
	}

	@Override
	public void processInputFromServer() {
		final String receivedCommand = communicator.receive();

		for ( String command : listenersWithoutParams.keySet() ) {
			if ( receivedCommand.equals( command ) ) {
				listenersWithoutParams.get( command ).execute();
			}
		}

		for ( String command : stringParameterListeners.keySet() ) {
			if ( receivedCommand.startsWith( command ) ) {
				stringParameterListeners.get( command ).execute( receivedCommand.split( " " )[ 1 ] );
			}
		}

		for ( String command : intParameterListeners.keySet() ) {
			if ( receivedCommand.startsWith( command ) ) {
				intParameterListeners.get( command ).execute( Integer.parseInt( receivedCommand.split( " " )[ 1 ] ) );
			}
		}
	}
}
