package com.leokom.chess.engine;

import java.util.HashSet;
import java.util.Set;

/**
 * Current position on-board (probably with some historical data...)
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position {
	/**
	 * Add a pawn to the position
	 * @param side
	 * @param square
	 */
	public void addPawn( Side side, String square ) {
		//TODO: what if the square is already occupied?
	}

	public Set<String> getMovesFrom( String square ) {
		final HashSet<String> result = new HashSet<String>();
		char file = square.charAt( 0 ); //depends on format e2

		result.add( file + "3" );
		result.add( file + "4" );
		return result;
	}
}
