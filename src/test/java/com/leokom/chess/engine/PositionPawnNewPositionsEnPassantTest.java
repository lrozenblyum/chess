package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

/**
 * Test en passant cases for new position generation by pawn movement
 * Author: Leonid
 * Date-time: 11.09.12 22:28
 */
public class PositionPawnNewPositionsEnPassantTest {
	//file that gives en passant right
	private static final String fileMovedBefore = "c"; //any!
	private Position position;

	@Before
	public void prepare() {
		//TODO: add the actual pawn of correct color as well.
		this.position = new Position( fileMovedBefore );
	}


}
