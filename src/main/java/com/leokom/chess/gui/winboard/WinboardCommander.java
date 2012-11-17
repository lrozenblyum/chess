package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Low-level Winboard-commands for some abstraction
 * and easier testing.
 * However it's hard to judge if it's not intersecting
 * with WinboardController
 * Author: Leonid
 * Date-time: 10.11.12 21:54
 */
interface WinboardCommander {

	void startInit();

	/**
	 * TODO: think if it's must be the interface part
	 * or part of implementation details.
	 * As it has influence how the result will be parsed...
	 */
	void enableUserMovePrefixes();

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
}
