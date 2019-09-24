package com.leokom.games.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionSideTest {
	@Test
	public void initialSide() {
		assertEquals( Side.WHITE, Position.getInitialPosition().getSideToMove() );
	}

	@Test
	public void sideAfterMove() {
		assertEquals( Side.BLACK, Position.getInitialPosition().move( new Move( "e2", "e4" ) ).getSideToMove() );
	}
}