package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Controller;

/**
 * Create Winboard-related stuff
 * Author: Leonid
 * Date-time: 20.08.12 20:19
 */
public final class WinboardFactory {
	private WinboardFactory() {}

	/**
	 * @return instance of properly initialized WinboardController
	 */
	public static Controller getController() {
		//TODO: if in any application place we'll use System.out.println or System.in.read
		//this may damage Winboard behaviour. The easiest way to fix it is to redirect System.out, System.in calls
		//to anything else (Logger?) and use the 'standard' in/out only inside WinboardController

		//TODO: implement some singleton policy?
		final WinboardCommunicator communicator = new WinboardCommunicator( System.in, System.out );
		return new WinboardController( communicator, new WinboardCommanderImpl( communicator ) );
	}
}
