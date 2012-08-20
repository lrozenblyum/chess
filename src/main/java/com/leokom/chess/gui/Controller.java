package com.leokom.chess.gui;

import com.leokom.chess.gui.Listener;

/**
 * Author: Leonid
 * Date-time: 20.08.12 20:48
 */
public interface Controller {
	//may create attach - now it's over-projecting - 1 is OK
	void setOnMoveListener( Listener listenerToSet );

	void run();

	void send( String command );
}
