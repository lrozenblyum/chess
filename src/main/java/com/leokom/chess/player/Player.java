package com.leokom.chess.player;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player extends ResignListener {
	//TODO: think if it's player's property
	void run();

	void opponentOfferedDraw();
	void opponentAgreedToDrawOffer();

	/**
	 * React on another player's move
	 * If it's null - means it's our first move
	 * which we must execute
	 * @param opponentMove move received from the opponent, or null
	 */
	void opponentMoved( String opponentMove );

	//TODO: this method is extracted because we need
	//to set up bidirectional connection
	//think about better solution
	//(this is not good because player without opponent is in
	//half-constructed state)
	void setOpponent( Player opponent );
}
