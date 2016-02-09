package com.leokom.chess.player;

import com.leokom.chess.engine.Move;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player {
	/**
	 *
	 * The method should be starting point to move for whites
	 *
	 * Black player should understand that the opponent suggests
	 * him start new game, and play white in this game.
	 *
	 */
	void opponentSuggestsMeStartNewGameWhite();

	void opponentSuggestsMeStartNewGameBlack();

	/**
	 * React on another player's move
	 *
	 * @param opponentMove move received from the opponent
	 */
	void opponentMoved( Move opponentMove );

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
	 * Playing mode is inspired by Winboard 'go' command.
	 * From this moment the player joins the game playing for the side
	 * whose move is right now.
	 *
	 */
	void switchToPlayingMode();


}
