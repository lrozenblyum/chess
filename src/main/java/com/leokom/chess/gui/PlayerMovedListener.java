package com.leokom.chess.gui;

/**
 * General listener to some command received
 * Author: Leonid
 * Date-time: 20.08.12 19:38
 */
public interface PlayerMovedListener {
	//for sure in next versions it may be some Command object so that
	//it's NOT dependent on xboard.
	void onPlayerMoved();
}
