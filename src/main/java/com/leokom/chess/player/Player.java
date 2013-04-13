package com.leokom.chess.player;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player extends NeedToGoListener, DrawOfferedListener, ResignListener {
	//may create attach - now it's over-projecting - 1 is OK

	/* The 'on' listeners
	 * represent the part of player that may be taught */

 	/**
	 * Set up listener for the event 'moved'.
	 * It means: current player has executed the move
	 * and informs the interested subscriber (currently only 1)
	 * about this
	 * @param needToGoListenerToSet the most important listener for move execution
	 */
	void onOpponentMoved( NeedToGoListener needToGoListenerToSet );

	void onOpponentOfferedDraw( DrawOfferedListener listener );

	//TODO: think if it's player's property
	void run();

	void opponentAgreedToDrawOffer();
}
