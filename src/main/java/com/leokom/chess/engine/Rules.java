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
class Rules {
	private static final Rules BEFORE_JULY_2014 = new Rules();
	//TODO: inject new rules after they're implemented
	//keeping old behaviour so far
	static final Rules DEFAULT = BEFORE_JULY_2014;

	private Rules() {
	}

	OptionalInt getMovesTillDraw() {
		//see discussion http://stackoverflow.com/questions/26364330/why-isnt-there-an-optionalint-ofnullableinteger
		return OptionalInt.empty();
	}
}
