package com.leokom.chess.player;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player {
	void opponentOfferedDraw();
	void opponentAgreedToDrawOffer();

	/**
	 *
	 * The method should be starting point to move for whites
	 * Black player shouldn't react to this message (out of symmetry)
	 *
	 */
	void opponentSuggestsMeStartGame();

	/**
	 * React on another player's move
	 *
	 * @param opponentMove move received from the opponent
	 *
	 * Move must be specified via notation (case-sensitive!) specified by examples:
	 * Normal move: e2e4
	 * Pawn promotion: e7e8Q
	 * Castling: e1g1
	 */
	//REFACTOR: pass Move class instead of string?
	void opponentMoved( String opponentMove );
	void opponentResigned();

	//TODO: this method is extracted because we need
	//to set up bidirectional connection
	//think about better solution
	//(this is not good because player without opponent is in
	//half-constructed state)
	void setOpponent( Player opponent );

}
