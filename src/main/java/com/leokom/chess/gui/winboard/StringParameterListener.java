package com.leokom.chess.gui.winboard;

/**
 * Ensure int-parameters listeners share common interface (thus easy-to-use)
 */
interface StringParameterListener {
	void execute( String stringParameterToPass );
}
