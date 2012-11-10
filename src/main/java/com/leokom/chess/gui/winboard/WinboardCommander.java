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
	 *
	 * Immediately switches Winboard engine in 'features set up mode'
	 *
	 * @param communicator low-level framework to use to send/receive the commands
	 */
	public WinboardCommander( Communicator communicator ) {
		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		communicator.send( "feature done=0" );
	}
}
