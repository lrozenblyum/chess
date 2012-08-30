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
		Position position = createPositionWithoutEnPassantRight();

		position.addPawn( Side.BLACK, "f5" );

		testPawn( position, "e5", Side.WHITE, "e6" );
	}

	@Test
	public void noEnpassantInSimilarPositionBlack() {
		Position position = createPositionWithoutEnPassantRight();
		position.addPawn( Side.WHITE, "c4" );

		testPawn( position, "d4", Side.BLACK, "d3" );
	}

	@Test
	public void enPassantInActionRightSideCapture() {
		Position position = createPositionWithEnPassantPossibility( "f" );

		position.addPawn( Side.BLACK, "f5" );

		//TODO: need any indication in 'f6' result as en passant?
		testPawn( position, "e5", Side.WHITE, "e6", "f6" );
	}

	@Test
	public void enPassantInActionLeftSideCapture() {
		Position position = createPositionWithEnPassantPossibility( "c" );

		position.addPawn( Side.BLACK, "c5" );

		testPawn( position, "d5", Side.WHITE, "d6", "c6" );
	}

	@Test
	public void enPassantInActionRightSideCaptureAnother() {
		Position position = createPositionWithEnPassantPossibility( "g" );

		position.addPawn( Side.BLACK, "g5" );

		testPawn( position, "f5", Side.WHITE, "f6", "g6" );
	}

	@Test
	public void enPassantInActionButNotWorksOnDifferentRank() {
		Position position = createPositionWithEnPassantPossibility( "c" );
		position.addPawn( Side.BLACK, "c5" );

		testPawn( position, "f5", Side.WHITE, "f6" );
	}

	private Position createPositionWithoutEnPassantRight() {
		return new Position( null );
	}

	/**
	 * Create a position, indicating the previous move was done
	 * by pawn as double-move from initial position to the file provided, so
	 * en passant is legal
	 * @param file
	 * @return
	 */
	private Position createPositionWithEnPassantPossibility( String file ) {
		return new Position( file );
	}
}
