package com.leokom.chess.engine;

/**
 * Value object representing act of moving
 *
 * Author: Leonid
 * Date-time: 06.07.13 22:38
 */
//REFACTOR: use this class on all applicable layers
//where pair of 'squareFrom, to' are used
public final class Move {
	/**
	 * Size of promotion move (e.g. "h1Q")
	 */
	private static final int PROMOTION_MOVE_SIZE = 3;
	private final String from;
	private final String to;

	/**
	 * Create an 'act of moving'
	 * @param squareFrom square like e6
	 * @param moveDestination move destination like e8R or d7
	 */
	public Move( String squareFrom, String moveDestination ) {
		//TODO: validate not null? correctness?
		this.from = squareFrom;
		this.to = moveDestination;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	/**
	 * Usual equals for value object
	 * @param object other move to compare this to
	 * @return true if from & to fields of rhs are equal to ours
	 */
	@Override
	public boolean equals( Object object ) {
		if ( ! ( object instanceof Move ) ) {
			return false;
		}

		Move another = ( Move ) object;
		return this.from.equals( another.from ) && this.to.equals( another.to );
	}

	//implementing this due to equals
	@Override
	public int hashCode() {
		//not important to have 'more clever' implementation so far

		//return 0 gives too predictable results for LegalPlayer
		//while for us it shouldn't be a motivation to make any sorting for external player
		return 0;
	}

	@Override
	public String toString() {
		return from + " : " + to;
	}

	static boolean isPromotion( String move ) {
		return move.length() == PROMOTION_MOVE_SIZE;
	}

	/**
	 * Get destination square from the move
	 *
	 * @param move in format like e2 or f1Q
	 * @return destination square (e2 or f1 correspondingly)
	 */
	static String getDestinationSquare( String move ) {
		return isPromotion( move ) ?
				move.substring( 0, 2 ) :
				move;
	}
}
