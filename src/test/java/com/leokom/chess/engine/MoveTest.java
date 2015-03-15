package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

public class MoveTest {
	@Test
	public void resignToString() {
		assertEquals( "RESIGN", Move.RESIGN.toString() );
	}
}