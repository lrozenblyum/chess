package com.leokom.chess.framework;

/**
 * Listener to fact that we need to go
 * Author: Leonid
 * Date-time: 20.08.12 19:38
 */
public interface NeedToGoListener {
	//for sure in next versions it may be some Command object so that
	//it's NOT dependent on xboard.

	/**
	 * React on another player's move
	 * If it's null - means it's our first move
	 * which we must execute
	 * @param anotherPlayerMove
	 */
	void anotherPlayerMoved( String anotherPlayerMove );
}
