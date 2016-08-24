package com.leokom.chess.engine;

import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;

/**
 * Represent chess-board (files/ranks/squares) but not position
 * Author: Leonid
 * Date-time: 15.09.12 18:48
 */
final class Board {

	static final char MINIMAL_FILE = 'a';
	static final char MAXIMAL_FILE = 'h';

	//so far it's not intended to create the boards,
	//however need to rethink this approach...
	private Board() {}

	/**
	 * Minimal rank from FIDE rules
	 */
	static final int MINIMAL_RANK = 1;
	/**
	 * Maximal rank from FIDE rules
	 */
	static final int MAXIMAL_RANK = 8;

	/**
	 * Get file where square is situated
	 * @param square square in format 'e2'
	 * @return file of square
	 */
	static char fileOfSquare( String square ) {
		return square.charAt( 0 );
	}

	static int rankOfSquare( String square ) {
		return Character.getNumericValue( square.charAt( 1 ) );
	}

	private static char fileTo( char file, HorizontalDirection direction, int shift ) {
		switch ( direction ) {
			case LEFT:
				return (char) ( file - shift );
			case RIGHT:
				return (char) ( file + shift );
			default:
				throw new IllegalArgumentException( "Direction is not supported: " + direction );
		}
	}

	//the cache has been introduced according to profiler's result:
	//string concatenation was rather slow
	private static final Table< Character, Integer, String > SQUARES;
	static {
		final ImmutableTable.Builder<Character, Integer, String> tableBuilder = new ImmutableTable.Builder<>();

		for ( char file = MINIMAL_FILE; file <= MAXIMAL_FILE; file++ ) {
			for ( int rank = MINIMAL_RANK; rank <= MAXIMAL_RANK; rank++ ) {
				tableBuilder.put( file, rank, String.valueOf( file ) + rank  );
			}
		}

		SQUARES = tableBuilder.build();
	}

	static String squareTo( String square, HorizontalDirection horizontalDirection, int horizontalShift, VerticalDirection verticalDirection, int verticalShift ) {
		char file = fileOfSquare( square );
		int rank = rankOfSquare( square );

		char destinationFile = fileTo( file, horizontalDirection, horizontalShift );
		int destinationRank = rankTo( rank, verticalDirection, verticalShift );

		//validity check
		if ( isFileValid( destinationFile ) && isRankValid( destinationRank ) ) {
			return square( destinationFile, destinationRank );
		}
		else {
			//TODO: maybe introduce some class Square, with Null object instance?
			return null;
		}
	}

	static String square( char destinationFile, int destinationRank ) {
		//optimized version of 'a' + 1 ==> "a1"
		//that is slow according to profiler
		//this also reduces pressure on GC
		return SQUARES.get( destinationFile, destinationRank );
	}

	private static String squareTo( String square, HorizontalDirection horizontalDirection ) {
		return squareTo( square, horizontalDirection, 1 );
	}

	private static String squareTo( String square, HorizontalDirection horizontalDirection, int horizontalShift ) {
		return squareTo( square, horizontalDirection, horizontalShift, VerticalDirection.UP, 0 );
	}

	private static String squareTo( String square, VerticalDirection verticalDirection ) {
		//the intermediate 2 params are unimportant. Need to improve
		return squareTo( square, HorizontalDirection.LEFT, 0, verticalDirection, 1 );
	}

	static String squareTo( String square, Direction direction ) {
		switch ( direction ) {
			case UP: return squareTo( square, VerticalDirection.UP );
			case DOWN: return squareTo( square, VerticalDirection.DOWN );
			case LEFT: return squareTo( square, HorizontalDirection.LEFT );
			case RIGHT: return squareTo( square, HorizontalDirection.RIGHT );
			default: throw new IllegalArgumentException( "Unsupported direction: " + direction );
		}
	}


	private static boolean isRankValid( int destinationRank ) {
		return destinationRank >= MINIMAL_RANK && destinationRank <= MAXIMAL_RANK;
	}

	private static boolean isFileValid( char file ) {
		//TODO: is character order guaranteed in Java for such comparisons?
		return file >= MINIMAL_FILE && file <= MAXIMAL_FILE;
	}

	private static int rankTo( int rank, VerticalDirection verticalDirection, int verticalShift ) {
		return
			verticalDirection == VerticalDirection.UP ?
			rank + verticalShift :
			rank - verticalShift;
	}

	static int rankTo( int rank, VerticalDirection verticalDirection ) {
		return rankTo( rank, verticalDirection, 1 );
	}

	/**
	 *
	 * @param firstSquare first square
	 * @param secondSquare second square
	 * @return true if squares are on the same file
	 */
	static boolean sameFile( String firstSquare, String secondSquare ) {
		return fileOfSquare( firstSquare ) == fileOfSquare( secondSquare );
	}

	private static String squareDiagonally( String square, HorizontalDirection horizontalDirection, VerticalDirection verticalDirection, int squaresDiagonally ) {
		return squareTo(
				square, horizontalDirection, squaresDiagonally, verticalDirection, squaresDiagonally );
	}

	static String squareDiagonally( String square, HorizontalDirection horizontalDirection, VerticalDirection verticalDirection ) {
		return squareDiagonally( square,horizontalDirection, verticalDirection, 1 );
	}
}
