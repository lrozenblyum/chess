package com.leokom.chess.engine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Current position on-board (probably with some historical data...)
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position {
	private static final int WHITE_PAWN_INITIAL_RANK = 2;
	private static final int BLACK_PAWN_INITIAL_RANK = 7;
	/**
	 * square -> side
	 */
	private Map< String, Side > sidesOccupied = new HashMap<String, Side>();

	/**
	 * Add a pawn to the position
	 * @param side
	 * @param square
	 */
	public void addPawn( Side side, String square ) {
		//TODO: what if the square is already occupied?
		sidesOccupied.put( square, side );
	}

	/**
	 * Get moves that are available from the square provided
	 * @param square square currently in format like 'e2'
	 * @return not-null set of available moves from square (could be empty for sure)
	 * TODO: what if square doesn't contain any pieces?
	 */
	public Set<String> getMovesFrom( String square ) {
		final Set<String> result = new HashSet<String>();
		String file = String.valueOf( square.charAt( 0 ) ); //depends on format e2

		//TODO: this internal conversion is needed because char itself has its
		//numeric value
		final int rank = Integer.valueOf( String.valueOf(square.charAt( 1 ) ));

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = sidesOccupied.get( square );
		switch ( side ) {
			case WHITE:
				final int higherRank = rank + 1;
				result.add( file + higherRank );
				if ( rank == WHITE_PAWN_INITIAL_RANK ) {
					result.add( file + ( rank + 2 ) );
				}

				//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
				//because it simply cannot find 'i' file result - it's null... I don't like such side effects

				addIfOccupiedByBlack( result, fileToRight( file ) + higherRank );
				addIfOccupiedByBlack( result, fileToLeft( file ) + higherRank );

				break;
			case BLACK:
				final int lowerRank = rank - 1;
				result.add( file + lowerRank );
				if ( rank == BLACK_PAWN_INITIAL_RANK ) {
					result.add( file + ( rank - 2 ) );
				}

				addIfOccupiedByWhite( result, fileToRight( file ) + lowerRank );
				addIfOccupiedByWhite( result, fileToLeft( file ) + lowerRank );

				break;
		}

		return result;

	}

	private String fileToLeft( String file ) {
		//TODO: UGLY construction, need better!
		return String.valueOf( (char) ( file.charAt( 0 ) - 1 ) );
	}

	private String fileToRight( String file ) {
		return String.valueOf( (char) ( file.charAt( 0 ) + 1 ) );
	}

	/**
	 * Add the square to result IFF it's occupied by black!
	 * @param result
	 * @param square
	 */
	private void addIfOccupiedByBlack( Set<String> result, String square ) {
		addIfOccupiedBy( result, square, Side.BLACK );
	}

	private void addIfOccupiedByWhite( Set<String> result, String square ) {
		addIfOccupiedBy( result, square, Side.WHITE );
	}

	private void addIfOccupiedBy( Set<String> result, String square, Side side ) {
		if ( sidesOccupied.get( square ) != null &&
				sidesOccupied.get( square ) == side ) {
			result.add( square );
		}
	}
}
