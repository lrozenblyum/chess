package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import java.util.Optional;

/**
 * The part of brain the makes decision :
 * this is the move to execute in current position
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:53
 */
interface DecisionMaker {

	/**
	 * Finds the best move in the current position.
	 * @return best move according to current strategy, absence of moves means:
	 * no moves are legal - we reached a terminal position
	 */
	Optional< Move > findBestMove( Position position );
}
