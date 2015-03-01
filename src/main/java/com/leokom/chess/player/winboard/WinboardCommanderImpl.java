package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Side;

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
	private final Map<String, NoParametersListener> listenersWithoutParams = new HashMap<>();
	private final Map<String, StringParameterListener> stringParameterListeners = new HashMap<>();
	private final Map<String, IntParameterListener> intParameterListeners = new HashMap<>();

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
		//in our implementation we support the following part of specs:

		//"For the actual move text from your chess engine (in place of MOVE above), your move should be either
		//in coordinate notation (e.g., e2e4, e7e8q) with castling indicated by the King's two-square move (e.g., e1g1)"
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

		//"result RESULT {COMMENT}"
		//"The COMMENT string is purely a human-readable comment; its content is unspecified and subject to change."
		//TODO: really bad - no normal indication of RESIGN event??

		//looks not good:
		//"If you won but did not just play a mate, your opponent must have resigned or forfeited."

		listenersWithoutParams.put( "result 1-0 {Black resigns}", listener );
	}

	@Override
	public void checkmate( Side winningSide ) {
		String prefix = "";
		switch ( winningSide ) {
			case WHITE:
				prefix = "1-0";
				break;
			case BLACK:
				prefix = "0-1";
				break;
		}
		communicator.send( prefix + " {LeokomChess : checkmate}" );
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
		//server might send nothing. No problems.
		if ( receivedCommand == null ) {
			return;
		}

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
