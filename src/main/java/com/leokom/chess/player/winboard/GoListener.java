package com.leokom.chess.player.winboard;

/**
 * Listen to 'go' command
 *
 * 'go' tells the engine to start playing for the side that now has
 * the move (regardless of what it was doing before), and keep
 * spontaneously generating moves for that side each time that side
 * has to move again.
 */
interface GoListener extends NoParametersListener {
	void execute();
}
