package com.leokom.chess.player.winboard;

/**
 * Listen to Winboard's Quit command
 * Author: Leonid
 * Date-time: 17.11.12 22:11
 */
interface QuitListener extends NoParametersListener {
	@Override
	void execute();
}
