package com.leokom.chess.gui.winboard;

/**
 * <MOVE> means the move in coordinate notation (4 characters,
 * or 5 when a promotion is involved).
 */
interface UserMoveListener {
	//NOTE: move format is currently highly-coupled with Winboard...
	void execute( String move );
}
