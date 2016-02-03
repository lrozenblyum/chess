package com.leokom.chess.player.winboard;

/**
 * Ensure String-parameters listeners share common interface (thus easy-to-use)
 */
interface StringParameterListener {
	/**
	 * Listen to event with string parameter
	 * @param stringParameterToPass non-null parameter
	 */
	void execute( String stringParameterToPass );
}
