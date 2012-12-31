package com.leokom.chess.framework;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player extends PlayerMovedListener, DrawOfferedListener, ResignListener {
	//may create attach - now it's over-projecting - 1 is OK
	/**
	 * Set up listener for the event 'moved'.
	 * It means: current player has executed the move
	 * and informs the interested subscriber (currently only 1)
	 * about this
	 * @param playerMovedListenerToSet
	 */
	void onMoved( PlayerMovedListener playerMovedListenerToSet );

	//TODO: think if it's player's property
	void run();
}
