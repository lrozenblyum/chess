package com.leokom.chess.engine;

import java.util.Arrays;

/**
 * Immutable value object representing act of moving.
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

	/**
	 * Internal semantic representation of a special move
	 * Allows deeper logic than Move.java can provide
	 */
	private enum SpecialMove {
		RESIGN,
		OFFER_DRAW,
		ACCEPT_DRAW,
		CLAIM_DRAW;

		private final Move move;

		SpecialMove() {
			this.move = new Move();
		}

		Move get() {
			return this.move;
		}

		static SpecialMove fromMove( Move move ) {
			return Arrays.stream( SpecialMove.values() )
					.filter( specialMove -> specialMove.move == move )
					.findAny().
					orElseThrow( () -> new IllegalArgumentException( "Move is not special: " + move ) );
		}
	}

	public static final Move RESIGN = SpecialMove.RESIGN.get();
	public static final Move OFFER_DRAW = SpecialMove.OFFER_DRAW.get();
	public static final Move ACCEPT_DRAW = SpecialMove.ACCEPT_DRAW.get();
	//it's hard to decide whether we should introduce
	//claim draw by 50 moves rule & claim draw by threefold repetition
	//or just a common generic claim draw. The generic claim draw
	//is easier to implement if UI integration doesn't support more specific one
	//while the specifics are more semantical.
	//so far implementing the minimal decision

	/**
	 * The draw claim move represents a claim that is valid now.
	 * To represent a claim that will be valid with an accompanying move, a new move
	 * type should be introduced.
	 */
	public static final Move CLAIM_DRAW = SpecialMove.CLAIM_DRAW.get();
	private final String from;
	private final String to;

	private final boolean isSpecial;

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
		this.isSpecial = false;
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
		return this.isSpecial ?
				SpecialMove.fromMove( this ).toString() :
				from + " : " + to;
	}

	public boolean isPromotion() {
		return isPromotion( to );
	}

	static boolean isPromotion( String move ) {
		return move.length() == PROMOTION_MOVE_SIZE;
	}

	String getDestinationSquare() {
		return getDestinationSquare( to );
	}

	PieceType getPromotionPieceType() {
		return getPromotionPieceType( to );
	}

	static PieceType getPromotionPieceType( String move ) {
		//depends on 3-char format

		String promotionNotation = move.substring( 2 );
		return PieceType.byNotation( promotionNotation );
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
