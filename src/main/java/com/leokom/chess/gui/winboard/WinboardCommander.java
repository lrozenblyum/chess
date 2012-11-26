package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Low-level Winboard-commands for some abstraction
 * and easier testing.
 * Author: Leonid
 * Date-time: 10.11.12 21:54
 */
interface WinboardCommander {

	void startInit();

	/**
	 * TODO: think if it's needed in interface
	 * enable usermove prefixes for moves for easier parsing
	 */
	void enableUserMovePrefixes();

	//signal end of initializations
	void finishInit();

	void agreeToDrawOffer();

	void setProtoverListener( ProtoverListener protoverListener );
	void setQuitListener( QuitListener listener );

	/**
	 * Fetch input from the underlying communication component
	 * Call some listeners if any....
	 */
	void getInput();

	//TODO: temporarily exposing this via interface
	//to allow smooth step-by-step refactoring
	Communicator getCommunicator();

	void setGoListener( GoListener listener );

	void setUserMoveListener( UserMoveListener listener );

	void setOfferDrawListener( OfferDrawListener listener );

	//TODO: add analyze if this line is received immediately after xboard
	//if not - we may assume it's protocol v1
	void setXboardListener( XBoardListener listener );

	void anotherPlayerMoved( String move );

	void offerDraw();
}
