package com.leokom.chess.engine;

import java.util.OptionalInt;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Author: Leonid
 * Date-time: 27.03.16 17:32
 */
public class RulesBuilder {
	private final Rules rules;
	private int movesTillDraw = Rules.DEFAULT.getMovesTillDraw().orElse(1);
	private int movesTillClaimDraw = Rules.DEFAULT.getMovesTillClaimDraw();

	public RulesBuilder() {
		rules = mock( Rules.class );
	}

	public RulesBuilder movesTillDraw( int smallestPossibleCount ) {
		this.movesTillDraw = smallestPossibleCount;
		return this;
	}

	public RulesBuilder movesTillClaimDraw( int movesTillClaimDraw ) {
		this.movesTillClaimDraw = movesTillClaimDraw;
		return this;
	}

	public Rules build() {
		when( rules.getMovesTillDraw() ).thenReturn( OptionalInt.of( movesTillDraw ) );
		when( rules.getMovesTillClaimDraw() ).thenReturn( movesTillClaimDraw );
		return rules;
	}
}
