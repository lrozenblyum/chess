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

	static final int MINIMAL_RANK = 1; //by FIDE
	static final int MAXIMAL_RANK = 8; //by FIDE

	/**
	 * Depends on format e2
	 * @param square
	 * @return file of square
	 */
	static String fileOfSquare( String square ) {
		return String.valueOf( square.charAt( 0 ) );
	}

	static Integer rankOfSquare( String square ) {
		//this internal conversion is needed because char itself has its
		return Integer.valueOf( String.valueOf( square.charAt( 1 ) ));
	}

	static String fileToLeft( String file ) {
		//TODO: UGLY construction, need better!
		return String.valueOf( (char) ( file.charAt( 0 ) - 1 ) );
	}

	static String fileToRight( String file ) {
		return String.valueOf( (char) ( file.charAt( 0 ) + 1 ) );
	}
}
