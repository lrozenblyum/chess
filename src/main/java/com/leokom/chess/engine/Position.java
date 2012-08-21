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

	}

	public Set<String> getMovesFrom( String square ) {
		final HashSet<String> result = new HashSet<String>();
		result.add( "e3" );
		result.add( "e4" );
		return result;
	}
}
