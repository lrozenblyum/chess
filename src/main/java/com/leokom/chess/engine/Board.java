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
		switch ( direction ) {
			case LEFT:
				//TODO: UGLY construction, need better!
				return String.valueOf( (char) ( file.charAt( 0 ) - 1 ) );
			case RIGHT:
				return String.valueOf( (char) ( file.charAt( 0 ) + 1 ) );
			default:
				throw new IllegalArgumentException( "Direction is not supported: " + direction );
		}
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
