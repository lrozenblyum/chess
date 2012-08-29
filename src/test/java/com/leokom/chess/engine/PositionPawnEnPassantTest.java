package com.leokom.chess.engine;

import org.junit.Test;
import static com.leokom.chess.engine.PawnUtils.testPawn;

/**
 * Test en passant possibilities for pawns
 * Author: Leonid
 * Date-time: 29.08.12 22:20
 */
public class PositionPawnEnPassantTest {

	/*
	position looks like en passant, but this pawns structure
	doesn't allow by fact en passant (e.g. because it was lost 1 move before)
	*/
	@Test
	public void noEnPassantInSimilarPosition() {
		Position position = createPositionWithoutEnpassantRight();

		position.addPawn( Side.BLACK, "f5" );

		testPawn( position, "e5", Side.WHITE, "e6" );
	}


	private Position createPositionWithoutEnpassantRight() {
		return new Position();
	}
}
