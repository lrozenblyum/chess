package com.leokom.chess.player;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player {
	/**
	 *
	 * The method should be a starting point to move for whites.
	 *
	 * Returning from this method is expected only after the game end.
	 *
	 * If a black player receives this method call,
	 * he must understand that the opponent suggests
	 * him start new game, and play white in this game.
	 *
	 */
	void opponentSuggestsMeStartNewGameWhite();

	/**
	 * Prepare a new game for playing black.
	 * Wait for the first whites move.
	 */
	void opponentSuggestsMeStartNewGameBlack();

	/**
	 * React to another player's move (s).
	 *
	 * If recording mode is ON, simply update internal Position.
	 * In this case the move(s) is/are treated not from an opponent
	 * but just from some external source (we might receive moves
	 * both for WHITE and for BLACK here).
	 *
	 * @param opponentMoves moves received from the opponent.
*             Several moves can be received for example when the opponent offers draw
	 */
	void opponentMoved( Move ... opponentMoves );

	//TODO: this method is extracted because we need
	//to set up bidirectional connection
	//think about better solution
	//(this is not good because player without opponent is in
	//half-constructed state)
	void setOpponent( Player opponent );

	/**
	 * Recording mode is inspired by Winboard 'force' command.
	 * In this mode the player just monitors input of moves
	 * and doesn't execute any moves.
	 */
	void switchToRecodingMode();

	/**
	 * Leave recoding mode. This will allow in future actively reacting to the
	 * opponent's moves
	 */
	void leaveRecordingMode();

	/**
	 * Inspired by Winboard 'go' command.
	 * From this moment the player joins the game playing for the side
	 * whose move is right now.
	 *
	 */
	void joinGameForSideToMove();

	/**
	 * Get position from the perspective of the Player
	 * Practically before game start and after game end the Position representation
	 * of the players should be equal.
	 * During the game they might differ between moves when a player
	 * hasn't yet received a move from the opponent.
	 * @return position position which is not null
	 */
	Position getPosition();
}