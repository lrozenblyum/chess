package com.leokom.games.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static com.leokom.games.chess.engine.PositionAsserts.*;
import static com.leokom.games.chess.engine.PositionUtils.addAny;
import static com.leokom.games.chess.engine.PositionUtils.addCapturable;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Generate positions by legal pawn moves using the initial position
 * Author: Leonid
 * Date-time: 31.08.12 22:05
 */
public class PawnNewPositionsTest {
	private PositionBuilder position;

	@Before
	public void prepare() {
		position = new PositionBuilder();
	}

	@Test
	public void basicContractRequirements() {
		final String anyInitialSquare = "g3";
		final String anyValidSquareToMove = "g4";
		Position position = new PositionBuilder().addPawn(Side.WHITE, anyInitialSquare).build();

		Position newPosition = position.move( anyInitialSquare, anyValidSquareToMove );
		assertNotNull( "New position must be not null", newPosition );
		assertNotSame( newPosition, position);
	}

	@Test
	public void singleMove() {
		final String anySquare = "c3";
		final Side side = Side.WHITE;

		final String squareToMove = "c4";

		assertPawnMovement( position, side, anySquare, squareToMove );
	}

	@Test
	public void singleMoveFromInitialPosition() {
		final String initialSquare = "e2";
		final Side side = Side.WHITE;
		final String squareToMove = "e3";

		assertPawnMovement( position, side, initialSquare, squareToMove );
	}

	@Test
	public void singleBlackMove() {
		final String initialSquare = "f4";
		final Side side = Side.BLACK;
		final String squareToMove = "f3";

		assertPawnMovement( position, side, initialSquare, squareToMove );
	}

	@Test
	public void doubleMove() {
		final String initialSquare = "c2";
		final Side side = Side.WHITE;
		final String squareToMove = "c4";

		assertPawnMovement( position, side, initialSquare, squareToMove );
	}

	@Test
	public void doubleBlackMove() {
		assertPawnMovement( position, Side.BLACK, "g7", "g5" );
	}

	@Test
	public void preserveOtherWhitePieces() {
		final Side notMovedPieceSide = Side.WHITE;
		final String notMovedPieceSquare = "g4";
		PieceType notMovedPieceType = addAny( position, notMovedPieceSide, notMovedPieceSquare );

		//side effect of moving
		Position newPosition = assertPawnMovement( position, Side.BLACK, "c6", "c5" );

		assertHasPiece( newPosition, notMovedPieceType, notMovedPieceSide, notMovedPieceSquare );
	}

	@Test
	public void preserveOtherWhitePiecesTriangulate() {
		final Side notMovedPieceSide = Side.WHITE;
		final String notMovedPieceSquare = "c4";

		PieceType notMovedPieceType = addAny( position, notMovedPieceSide, notMovedPieceSquare );

		Position newPosition = assertPawnMovement( position, Side.WHITE, "d3", "d4" );

		assertHasPiece( newPosition, notMovedPieceType, notMovedPieceSide, notMovedPieceSquare );
	}

	@Test
	public void preserveOtherBlackPieces() {
		final Side notMovedPieceSide = Side.BLACK;
		final String notMovedPieceSquare = "g4";

		PieceType notMovedPieceType = addAny( position, notMovedPieceSide, notMovedPieceSquare );

		//side effect of moving
		Position newPosition = assertPawnMovement( position, Side.BLACK, "d6", "d5" );

		assertHasPiece( newPosition, notMovedPieceType, notMovedPieceSide, notMovedPieceSquare );
	}

	@Test
	public void preserveCoupleOfPieces() {
		//TODO: Side+Square are duplicated too much
		//it means they must be coupled together!
		final Side firstAnySide = Side.BLACK;
		final String firstAnySquare = "f7";
		final Side secondAnySide = Side.WHITE; //any not depending on first
		final String secondAnySquare = "c3";

		PieceType notMovedPieceType = addAny( position, firstAnySide, firstAnySquare );
		PieceType notMovedPieceType2 = addAny( position, secondAnySide, secondAnySquare );

		Position newPosition = assertPawnMovement( position, Side.WHITE, "e2", "e4" );

		assertHasPiece( newPosition, notMovedPieceType, firstAnySide, firstAnySquare );
		assertHasPiece( newPosition, notMovedPieceType2, secondAnySide, secondAnySquare );
	}

	@Test
	public void captureLeft() {
		testCapture( Side.WHITE, Side.BLACK, "g4", "f5" );
	}

	@Test
	public void captureLeftTriangle() {
		testCapture( Side.WHITE, Side.BLACK, "d2", "c3" );
	}

	@Test
	public void captureRight() {
		testCapture( Side.WHITE, Side.BLACK, "f6", "g7" );
	}

	@Test
	public void captureLeftBlack() {
		testCapture( Side.BLACK, Side.WHITE, "c3", "b2" );
	}

	private void testCapture( Side movingSide, Side sideToCapture, String sourceSquare, String targetSquare ) {
		PositionBuilder positionBuilder =
			new PositionBuilder()
			.addPawn( movingSide, sourceSquare );

		addCapturable( positionBuilder, sideToCapture, targetSquare );

		Position position = positionBuilder.setSideOf(sourceSquare).build();

		Position newPosition = position.move( sourceSquare, targetSquare );
		assertHasPawn( newPosition, targetSquare, movingSide );
		assertEmptySquare( newPosition, sourceSquare );
		org.junit.Assert.assertEquals( movingSide.opposite(), newPosition.getSideToMove() );
	}

	/**
	 * Assert that:
	 * if we add a pawn to the #position
	 * and call Position#move method,
	 * the result will have empty initialSquare and pawn on squareToMove
	 * @param side
	 * @param initialSquare
	 * @param squareToMove
	 * @return newPosition for further asserts
	 */
	private Position assertPawnMovement( PositionBuilder positionBuilder, Side side, String initialSquare, String squareToMove ) {
		Position position = positionBuilder
				.addPawn(side, initialSquare)
				.setSideOf(initialSquare)
				.build();
		Position newPosition = position.move( initialSquare, squareToMove );

		assertHasPawn( newPosition, squareToMove, side );
		assertEmptySquare( newPosition, initialSquare );
		org.junit.Assert.assertEquals( side.opposite(), newPosition.getSideToMove() );
		return newPosition;
	}
}
