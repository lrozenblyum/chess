package com.leokom.chess.engine;

import java.util.OptionalInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Leonid
 * Date-time: 27.03.16 17:32
 */
public class RulesBuilder {
	private Rules rules;
	public RulesBuilder() {
		rules = mock( Rules.class );
	}

	public RulesBuilder movesTillDraw( int smallestPossibleCount ) {
		when( rules.getMovesTillDraw() ).thenReturn( OptionalInt.of( smallestPossibleCount ) );
		return this;
	}

	public Rules build() {
		return rules;
	}
}
