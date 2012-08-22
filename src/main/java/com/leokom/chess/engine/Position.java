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

	/**
	 * Get moves that are available from the square provided
	 * @param square square currently in format like 'e2'
	 * @return not-null set of available moves from square (could be empty for sure)
	 * TODO: what if square doesn't contain any pieces?
	 */
	public Set<String> getMovesFrom( String square ) {
		final HashSet<String> result = new HashSet<String>();
		char file = square.charAt( 0 ); //depends on format e2

		//TODO: this internal conversion is needed because char itself has its
		//numeric value
		final int row = Integer.valueOf( String.valueOf(square.charAt( 1 ) ));
		if ( row == 2 ) {
			result.add( file + "3" );
			result.add( file + "4" );
		}
		else {
			int rowForPawn = row + 1;
			result.add( String.valueOf( file ) + rowForPawn );
		}
		return result;
	}
}
