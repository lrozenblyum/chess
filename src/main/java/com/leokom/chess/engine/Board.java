package com.leokom.chess.engine;

/**
 * Represent chess-board (files/ranks/squares) but not position
 * Author: Leonid
 * Date-time: 15.09.12 18:48
 */
final class Board {
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
	static String fileOfSquare( String square ) {
		return String.valueOf( square.charAt( 0 ) );
	}

	static Integer rankOfSquare( String square ) {
		//this internal conversion is needed because char itself has its
		return Integer.valueOf( String.valueOf( square.charAt( 1 ) ));
	}

	static String fileTo( String file, HorizontalDirection direction ) {
		return fileTo( file, direction, 1 );
	}

	private static String fileTo( String file, HorizontalDirection direction, int shift ) {
		switch ( direction ) {
			case LEFT:
				//TODO: UGLY construction, need better!
				return String.valueOf( (char) ( file.charAt( 0 ) - shift ) );
			case RIGHT:
				return String.valueOf( (char) ( file.charAt( 0 ) + shift ) );
			default:
				throw new IllegalArgumentException( "Direction is not supported: " + direction );
		}
	}

	static String squareTo( String square, HorizontalDirection horizontalDirection, int horizontalShift, VerticalDirection verticalDirection, int verticalShift ) {
		String file = fileOfSquare( square );
		int rank = rankOfSquare( square );

		String destinationFile = fileTo( file, horizontalDirection, horizontalShift );
		int destinationRank = rankTo( rank, verticalDirection, verticalShift );

		//validity check
		if ( destinationFile.charAt( 0 ) >= 'a' && destinationFile.charAt( 0 ) <= 'h' &&
			destinationRank >= 1 && destinationRank <= 8 ) {

			return destinationFile + destinationRank;
		}
		else {
			//TODO: maybe introduce some class Square, with Null object instance?
			return null;
		}
	}

	private static int rankTo( int rank, VerticalDirection verticalDirection, int verticalShift ) {
		return
			verticalDirection == VerticalDirection.UP ?
			rank + verticalShift :
			rank - verticalShift;
	}

	/**
	 *
	 * @param firstSquare
	 * @param secondSquare
	 * @return true if squares are on the same file
	 */
	static boolean sameFile( String firstSquare, String secondSquare ) {
		return fileOfSquare( firstSquare ).equals( fileOfSquare( secondSquare ) );
	}
}
