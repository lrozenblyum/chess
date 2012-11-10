package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Middle-level command work for Winboard.
 * It must know about commands format.
 * Probably it mustn't know about the commands sequence
 * (but this will be clarified by TDD)
 *
 * Author: Leonid
 * Date-time: 10.11.12 21:22
 */
class WinboardCommander {
	/**
	 * Create the commander, with communicator injected
	 * @param communicator
	 */
	public WinboardCommander( Communicator communicator ) {
		communicator.send( "test" );
	}

	public void startInitialization() {


	}
}
