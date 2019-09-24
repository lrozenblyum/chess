package com.leokom.games.chess.engine;

import org.junit.Test;

import static com.leokom.games.chess.engine.PawnUtils.testPawn;

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
		PositionBuilder position = createPositionWithoutEnPassantRight();

		position.addPawn( Side.BLACK, "f5" );

		testPawn( position, "e5", Side.WHITE, "e6" );
	}

	@Test
	public void noEnPassantInSimilarPositionBlack() {
		PositionBuilder position = createPositionWithoutEnPassantRight();
		position.addPawn( Side.WHITE, "c4" );

		testPawn( position, "d4", Side.BLACK, "d3" );
	}

	//both sides are on e, black had double move last
	@Test
	public void noEnPassantInSymmetricCase() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'e' );
		position.addPawn( Side.WHITE, "e4" );
		position.addPawn( Side.BLACK, "e5" );

		testPawn( position, "f4", Side.WHITE, "e5", "f5" );
	}

	@Test
	public void twoSidedEnPassant() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'c' );
		position.addPawn( Side.BLACK, "c5" );

		position.addPawn( Side.WHITE, "b5" );
		position.addPawn( Side.WHITE, "d5" );

		testPawn( position, "b5", Side.WHITE, "b6", "c6" );
		testPawn( position, "d5", Side.WHITE, "d6", "c6" );
	}

	@Test
	public void noEnPassantSymmetricWhite() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'a' );
		position.addPawn( Side.WHITE, "a4" );
		position.addPawn( Side.BLACK, "a5" );

		testPawn( position, "b5", Side.BLACK, "b4", "a4" );
	}

	@Test
	public void enPassantInActionLeftSideCaptureBlack() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'c' );
		position.addPawn( Side.WHITE, "c4" );

		testPawn( position, "d4", Side.BLACK, "d3", "c3" );
	}

	@Test
	public void enPassantInActionRightSideCaptureBlack() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'g' );
		position.addPawn( Side.WHITE, "g4" );

		testPawn( position, "f4", Side.BLACK, "f3", "g3" );
	}

	@Test
	public void enPassantInActionRightSideCapture() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'f' );

		position.addPawn( Side.BLACK, "f5" );

		//TODO: need any indication in 'f6' result as en passant?
		testPawn( position, "e5", Side.WHITE, "e6", "f6" );
	}

	@Test
	public void enPassantInActionLeftSideCapture() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'c' );

		position.addPawn( Side.BLACK, "c5" );

		testPawn( position, "d5", Side.WHITE, "d6", "c6" );
	}

	@Test
	public void enPassantInActionRightSideCaptureAnother() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'g' );

		position.addPawn( Side.BLACK, "g5" );

		testPawn( position, "f5", Side.WHITE, "f6", "g6" );
	}

	@Test
	public void enPassantInActionButNotWorksOnDifferentRank() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'c' );
		position.addPawn( Side.BLACK, "c5" );

		testPawn( position, "f5", Side.WHITE, "f6" );
	}

	@Test
	public void enPassantCannotPutKingInCheck() {
		PositionBuilder position = new PositionBuilder().setEnPassantFile( 'g' );
		position
			.addPawn( Side.BLACK, "g5" )
			//rook will check king if we make en passant
			.add( Side.BLACK, "a5", PieceType.ROOK )
			.add( Side.WHITE, "h5", PieceType.KING );


		testPawn( position, "f5", Side.WHITE, "f6" );
	}

	private static PositionBuilder createPositionWithoutEnPassantRight() {
		return new PositionBuilder();
	}

}
