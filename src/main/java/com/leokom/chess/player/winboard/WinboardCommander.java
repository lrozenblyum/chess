package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Side;

/**
 * Low-level Winboard-commands for some abstraction and easier testing.
 * The 'on' methods set up listeners to some events received from Winboard server
 * Author: Leonid
 * Date-time: 10.11.12 21:54
 */
interface WinboardCommander {

	void startInit();

	/**
	 * Enable usermove prefixes for moves for easier parsing
	 */
	void enableUserMovePrefixes();

	//signal end of initializations
	void finishInit();

	void agreeToDrawOffer();

	/**
	 * Fetch input from the underlying communication component
	 * Dispatch listeners (call them depending on the input)
	 */
	void processInputFromServer();

	//TODO: add analyze if this command is received immediately after xboard
	//if not - we may assume it's protocol v1

	void onXBoard( XBoardListener listener );
	void opponentMoved( String move );

	void offerDraw();

	void resign();

	/**
	 * Inform Winboard that the side has won by checkmate
	 * @param side winning by checkmate side
	 */
	void checkmate( Side side );

	void onProtover( ProtoverListener protoverListener );
	void onQuit( QuitListener listener );
	void onGo( GoListener listener );
	void onUserMove( UserMoveListener listener );
	void onOfferDraw( OfferDrawListener listener );

	void onGameOver( GameOverListener listener );
}