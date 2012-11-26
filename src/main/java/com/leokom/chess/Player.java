package com.leokom.chess;

import com.leokom.chess.gui.PlayerMovedListener;

/**
 * Engine-agnostic player.
 * This interface is suggested to be common chess player abstraction
 * (independent of the fact the player is represented by a chess engine,
 * a human or a winboard-protocol )
 */
public interface Player {
	//may create attach - now it's over-projecting - 1 is OK
	//TODO: rename to new vision
	void setOnMoveListener( PlayerMovedListener playerMovedListenerToSet );

	//TODO: think if it's player's property
	void run();

	//TODO: definitely, this is too low-level for a player to implement
	void send( String command );
}
