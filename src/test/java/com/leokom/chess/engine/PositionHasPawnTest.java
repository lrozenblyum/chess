package com.leokom.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Develop position#hasPawn
 * Author: Leonid
 * Date-time: 01.09.12 21:35
 */
public class PositionHasPawnTest {
	private Position position;

	@Before
	public void prepare() {
		position = new Position( null );
	}

	@Test
	public void noPawn() {
		final String anySquare = "a2";
		assertFalse( position.hasPawn( anySquare, Side.WHITE ) );
		assertFalse( position.hasPawn( anySquare, Side.BLACK ) );
	}

	@Test
	public void presentPawn() {
		final String square = "g6";
		final Side side = Side.WHITE;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( square, side ) );
	}

	@Test
	public void absentPawnOfAnotherColor() {
		final String anySquare = "c6";
		final Side anySide = Side.WHITE;
		position.addPawn( anySide, anySquare );

		assertFalse( position.hasPawn( anySquare, anySide.opposite() ) );
	}

	@Test
	public void presentPawnInitial() {
		final String square = "a2";
		final Side side = Side.WHITE;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( square, side ) );
	}

	@Test
	public void presentBlackPawn() {
		final String square = "c4";
		final Side side = Side.BLACK;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( square, side ) );
	}

	//TODO: when new pieces are introduced:
	//add asserts that if we add such a piece on a square,
	//hasPawn will return FALSE!
}
