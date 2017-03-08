package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

import java.util.List;
/**
 * The brain.
 *
 * Makes decision :
 * this is the move(s) to execute in current position.
 *
 * A valid implementation of the decision maker must be
 * stateless.
 *
 * Author: Leonid
 * Date-time: 23.08.16 22:53
 */
public interface Brain {

	/**
	 * Finds the best move(s) in the current position.
	 * 2 moves can be returned if a player is allowed to execute
	 * something extra after his move, e.g. OFFER DRAW
	 * @param position position to analyze
	 * @return best move according to current strategy, absence of moves means:
	 * no moves are legal - we reached a terminal position
	 */
	List< Move > findBestMove( Position position );

	/**
	 * Get the best move to execute when it's not our
	 * turn to move
	 * @param position position to analyze
	 * @return best move in not our turn
	 * null if we don't want to move)
	 */
	/*
	That's exactly why default methods were introduced:
	to allow evolving interface while not forcing existing
	implementations to increase complexity
	 */
	default Move findBestMoveForOpponent( Position position ) {
		return null;
	}

	default String name() {
		return this.getClass().getSimpleName();
	}
}