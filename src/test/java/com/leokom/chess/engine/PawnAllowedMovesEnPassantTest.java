package com.leokom.chess.engine;

import org.junit.Test;

import static com.leokom.chess.engine.PawnUtils.testPawn;

/**
 * Test en passant possibilities for pawns
 * Author: Leonid
 * Date-time: 29.08.12 22:20
 */
public class PawnAllowedMovesEnPassantTest {

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
	public void noEnPassantInSimilarPositionBlack() {
		Position position = createPositionWithoutEnPassantRight();
		position.addPawn( Side.WHITE, "c4" );

		testPawn( position, "d4", Side.BLACK, "d3" );
	}

	//both sides are on e, black had double move last
	@Test
	public void noEnPassantInSymmetricCase() {
		Position position = createPositionWithEnPassantPossibility( "e" );
		position.addPawn( Side.WHITE, "e4" );
		position.addPawn( Side.BLACK, "e5" );

		testPawn( position, "f4", Side.WHITE, "e5", "f5" );
	}

	@Test
	public void twoSidedEnPassant() {
		Position position = createPositionWithEnPassantPossibility( "c" );
		position.addPawn( Side.BLACK, "c5" );

		position.addPawn( Side.WHITE, "b5" );
		position.addPawn( Side.WHITE, "d5" );

		testPawn( position, "b5", Side.WHITE, "b6", "c6" );
		testPawn( position, "d5", Side.WHITE, "d6", "c6" );
	}

	@Test
	public void noEnPassantSymmetricWhite() {
		Position position = createPositionWithEnPassantPossibility( "a" );
		position.addPawn( Side.WHITE, "a4" );
		position.addPawn( Side.BLACK, "a5" );

		testPawn( position, "b5", Side.BLACK, "b4", "a4" );
	}

	@Test
	public void enPassantInActionLeftSideCaptureBlack() {
		Position position = createPositionWithEnPassantPossibility( "c" );
		position.addPawn( Side.WHITE, "c4" );

		testPawn( position, "d4", Side.BLACK, "d3", "c3" );
	}

	@Test
	public void enPassantInActionRightSideCaptureBlack() {
		Position position = createPositionWithEnPassantPossibility( "g" );
		position.addPawn( Side.WHITE, "g4" );

		testPawn( position, "f4", Side.BLACK, "f3", "g3" );
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

	private static Position createPositionWithoutEnPassantRight() {
		return new Position( null );
	}

	/**
	 * Create a position, indicating the previous move was done
	 * by pawn as double-move from initial position to the file provided, so
	 * en passant is legal
	 * @param file
	 * @return
	 */
	private static Position createPositionWithEnPassantPossibility( String file ) {
		return new Position( file );
	}
}
