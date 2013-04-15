package com.leokom.chess.player.winboard;

import com.leokom.chess.player.Player;

/**
 * Create Winboard-related stuff
 * Author: Leonid
 * Date-time: 20.08.12 20:19
 */
public final class WinboardFactory {
	private WinboardFactory() {}

	/**
	 * Create an instance of generic Player,
	 * who is able to play against existing WinBoard-powered player
	 * This Winboard-powered opponent could be a human
	 * running the WinBoard-powered software or another engine
	 * that can communicate via Winboard protocol
	 *
	 * @return instance of properly initialized Player against WinBoard-powered player
	 *
	 */
	public static Player getPlayer() {
		//TODO: implement some singleton policy?
		final WinboardCommunicator communicator = new WinboardCommunicator();
		return new WinboardPlayer( new WinboardCommanderImpl( communicator ) );
	}
}
