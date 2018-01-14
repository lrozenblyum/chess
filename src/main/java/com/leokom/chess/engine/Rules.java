package com.leokom.chess.engine;

import java.util.OptionalInt;

/**
 * Immutable class-strategy to represent rules of chess that
 * has been changed recently and thus require injection
 * to support both old and new values.
 *
 *
 *
 * Author: Leonid
 * Date-time: 20.03.16 11:05
 */
public class Rules {
	private static final int MOVES_TILL_CLAIM_DRAW = 1;

	static final Rules BEFORE_JULY_2014 = new Rules();
	private static final Rules AFTER_JULY_2014 = new Rules( 75 );
	static final Rules DEFAULT = AFTER_JULY_2014;
	private final Integer countOfMovesTillDraw;

	private Rules() {
		this( null );
	}

	private Rules( Integer countOfMovesTillDraw ) {
		this.countOfMovesTillDraw = countOfMovesTillDraw;
	}

	public OptionalInt getMovesTillDraw() {
		//see discussion http://stackoverflow.com/questions/26364330/why-isnt-there-an-optionalint-ofnullableinteger
		return countOfMovesTillDraw == null ?
				OptionalInt.empty() :
				OptionalInt.of( countOfMovesTillDraw );
	}

	public int getMovesTillClaimDraw() {
		return MOVES_TILL_CLAIM_DRAW;
	}
}