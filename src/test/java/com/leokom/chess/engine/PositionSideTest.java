package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionSideTest {
	@Test
	public void initialSide() {
		assertEquals( Side.WHITE, Position.getInitialPosition().getSideToMove() );
	}
}