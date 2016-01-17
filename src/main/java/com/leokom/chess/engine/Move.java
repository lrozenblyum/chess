package com.leokom.chess.engine;

/**
 * Value object representing act of moving.
 * It encapsulates both moves that cause changes on-board
 * and acts that cause some other changes in game situation
 * like Resign.
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
	public static final Move RESIGN = new Move();
	public static final Move OFFER_DRAW = new Move();
	public static final Move ACCEPT_DRAW = new Move();
	private final String from;
	private final String to;

	private boolean isSpecial;

	/**
	 * Create an 'act of moving'
	 *
	 * Move must be specified via notation (case-sensitive!) specified by examples:
	 * (before space there is squareFrom parameter, after space moveDestination)
	 *
	 * Normal move: e2 e4
	 * Pawn promotion: e7 e8Q
	 * Castling: e1 g1
	 *
	 * @param squareFrom square like e6
	 * @param moveDestination move destination like e8R or d7
	 */
	public Move( String squareFrom, String moveDestination ) {
		//TODO: validate not null? correctness?
		this.from = squareFrom;
		this.to = moveDestination;
	}

	/**
	 * By 'special' move here we mean an act
	 * that doesn't cause movement of pieces on the board
	 * (example : RESIGN)
	 *
	 * @return true if the move is 'special'
	 *
	 */
	public boolean isSpecial() {
		return isSpecial;
	}

	private Move() {
		//TODO: ugly? This keeps them final
		//but they're unneeded for us
		//as as 'good' side effect it allows equal be correct
		//till we have just 1 special move (RESIGN)
		this.from = this.to = "";
		this.isSpecial = true;
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

		//special moves are implemented as singletons
		//and should be equal by reference
		if ( this.isSpecial ) {
			return object == this;
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
		return this == RESIGN ? "RESIGN" :
				this == OFFER_DRAW ? "OFFER_DRAW" :
				this == ACCEPT_DRAW ? "ACCEPT_DRAW" :
				from + " : " + to;
	}

	public boolean isPromotion() {
		return isPromotion( to );
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

	/**
	 * Helper method to convert the move object to old 'conventional'
	 * string presentation of the move
	 * @return old conventional string presentation of the move
	 */
	public String toOldStringPresentation() {
		return getFrom() + getTo();
	}
}
