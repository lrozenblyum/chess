package com.leokom.games.chess.player.winboard;

/**
 * <MOVE> means the move in coordinate notation (4 characters,
 * or 5 when a promotion is involved).
 */
interface UserMoveListener extends StringParameterListener {
	//NOTE: move format is currently highly-coupled with Winboard...
	@Override
	void execute( String move );
}
