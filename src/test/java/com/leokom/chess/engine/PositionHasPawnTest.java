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
		position = new Position();
	}

	@Test
	public void noPawn() {
		final String anySquare = "a2";
		assertFalse( position.hasPawn( Side.WHITE, anySquare ) );
		assertFalse( position.hasPawn( Side.BLACK, anySquare ) );
	}

	@Test
	public void presentPawn() {
		final String square = "g6";
		final Side side = Side.WHITE;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( side, square ) );
	}

	@Test
	public void absentPawnOfAnotherColor() {
		final String anySquare = "c6";
		final Side anySide = Side.WHITE;
		position.addPawn( anySide, anySquare );

		assertFalse( position.hasPawn( anySide.opposite(), anySquare ) );
	}

	@Test
	public void presentPawnInitial() {
		final String square = "a2";
		final Side side = Side.WHITE;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( side, square ) );
	}

	@Test
	public void presentBlackPawn() {
		final String square = "c4";
		final Side side = Side.BLACK;
		position.addPawn( side, square );
		assertTrue( position.hasPawn( side, square ) );
	}

	@Test
	public void addedQueenNoHasPawn() {
		final String square = "a8"; //any
		final Side side = Side.BLACK; //any
		position.addQueen( side, square );
		assertFalse( position.hasPawn( side, square ) );
	}

	@Test
	public void addedQueenFound() {
		final String square = "c1"; //any
		final Side side = Side.WHITE; //any
		position.addQueen( side, square );
		assertTrue( position.hasQueen( side, square ) );
	}

	@Test
	public void absentQueenNotFound(){
		final String square = "b4"; //any
		final Side side = Side.BLACK; //any
		assertFalse( position.hasQueen( side, square ) );
	}

	@Test
	public void queenOfOppositeSideNotFound() {
		final String square = "c1"; //any
		final Side side = Side.WHITE; //any
		position.addQueen( side, square );
		assertFalse( position.hasQueen( side.opposite(), square ) );
	}

	@Test
	public void differentSquareOfQueenNotFound() {
		final String square = "b4"; //any
		final String anotherSquare = "c1";
		final Side side = Side.BLACK; //any
		position.addQueen( side, square );
		assertFalse( position.hasQueen( side, anotherSquare ) );
	}

	@Test( expected = IllegalArgumentException.class )
	public void cannotAddPawnToWrongSquare() {
		position.addPawn( Side.WHITE, "h8Q" );
	}

	//TODO: when new pieces are introduced:
	//add asserts that if we add such a piece on a square,
	//hasPawn will return FALSE!
}
