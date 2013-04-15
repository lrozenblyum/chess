package com.leokom.chess.player.winboard;

/**
 * <MOVE> means the move in coordinate notation (4 characters,
 * or 5 when a promotion is involved).
 */
interface UserMoveListener extends StringParameterListener {
	//NOTE: move format is currently highly-coupled with Winboard...
	void execute( String move );
}
