package com.leokom.games.chess.engine;

import org.junit.Before;
import org.junit.Test;

import static com.leokom.games.chess.engine.PawnUtils.testPawn;

/**
 * Single position-based pawn tests
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class PawnAllowedMovesTest {
	private PositionBuilder position;

	@Before
	public void prepare() {
		position = new PositionBuilder();
	}

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void singlePawnInitialPosition() {
		testPawn( position, "e2", Side.WHITE, "e3", "e4" );
	}

	/**
	 * FIDE 3.7b
	 */
	@Test
	public void singlePawnAnother() {
		testPawn( position, "d2", Side.WHITE, "d3", "d4" );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMove() {
		testPawn( position, "d3", Side.WHITE, "d4" );
	}

	@Test
	public void singleCapturePossibleFromWhite() {
		PositionUtils.addCapturable( position, Side.BLACK, "e3" );

		testPawn( position, "d2", Side.WHITE, "d3", "d4", "e3" );
	}

	@Test
	public void singleCaptureImPossibleFromWhiteColorsCoincide() {
		final String pawnPosition = "d2";

		PositionUtils.addCapturable( position, Side.WHITE, "e3" );    //our color - cannot capture for sure!

		testPawn( position, pawnPosition, Side.WHITE, "d3", "d4" );
	}

	@Test
	public void captureFromNotInitialPositionRightSide() {
		final String sourceSquare = "g5";
		final String victimPawnSquare = "h6";

		PositionUtils.addCapturable( position, Side.BLACK, victimPawnSquare );

		testPawn( position, sourceSquare, Side.WHITE, "g6", victimPawnSquare );
	}

	@Test
	public void rightMostFileCapturingAtRightSideImpossible() {
		final String sourceSquare = "h4";
		
		testPawn( position, sourceSquare, Side.WHITE, "h5" );
	}

	@Test
	public void leftCapture() {
		final String sourceSquare = "d6";
		final String victimSquare = "c7";

		PositionUtils.addCapturable( position, Side.BLACK, victimSquare );

		testPawn( position, sourceSquare, Side.WHITE, "d7", victimSquare );
	}

	@Test
	public void pawnCanCaptureKnight() {
		final String sourceSquare = "d6";
		final String victimSquare = "c7";

		position.add( Side.BLACK, victimSquare, PieceType.KNIGHT );

		testPawn( position, sourceSquare, Side.WHITE, "d7", victimSquare );
	}


	@Test
	public void leftCaptureImpossibleWhitePiece() {
		final String sourceSquare = "e6";
		final String victimSquareFailed = "d7";

		PositionUtils.addCapturable( position, Side.WHITE, victimSquareFailed );

		testPawn( position, sourceSquare, Side.WHITE, "e7" );
	}

	@Test
	public void leftMostCaptureAbsent() {
		final String sourceSquare = "a3";

		testPawn( position, sourceSquare, Side.WHITE, "a4" );
	}

	@Test
	public void twoSidedCapture() {
		final String sourceSquare = "f6";
		final String firstVictim = "e7";
		final String secondVictim = "g7";

		PositionUtils.addCapturable( position, Side.BLACK, firstVictim );
		PositionUtils.addCapturable( position, Side.BLACK, secondVictim );

		testPawn( position, sourceSquare, Side.WHITE, "f7", firstVictim, secondVictim );
	}

	//maximally possible squares to move by pawn
	@Test
	public void twoSidedCaptureFromInitialPosition() {
		final String sourceSquare = "b2";
		final String firstVictim = "c3";
		final String secondVictim = "a3";

		PositionUtils.addCapturable( position, Side.BLACK, firstVictim );
		PositionUtils.addCapturable( position, Side.BLACK, secondVictim );

		testPawn( position, sourceSquare, Side.WHITE, "b3", "b4", firstVictim, secondVictim );
	}

	@Test
	public void blackRightCapture() {
		final String source = "g6";
		final String victim = "h5";

		PositionUtils.addCapturable( position, Side.WHITE, victim );

		testPawn( position, source, Side.BLACK, "g5", "h5" );
	}

	@Test
	public void blackRightCaptureImpossibleBlackPieceThere() {
		final String source = "c4";
		final String failedVictim = "d3";

		PositionUtils.addCapturable( position, Side.BLACK, failedVictim );

		testPawn( position, source, Side.BLACK, "c3" );
	}

	@Test
	public void blackLeftCapture() {
		final String source = "c4";
		final String victim = "b3";

		PositionUtils.addCapturable( position, Side.WHITE, victim );

		testPawn( position, source, Side.BLACK, "c3", victim );
	}

	@Test
	public void blackLeftCaptureImpossibleFilledByBlack() {
		final String source = "c4";
		final String failedVictim = "b3";

		PositionUtils.addCapturable( position, Side.BLACK, failedVictim );

		testPawn( position, source, Side.BLACK, "c3" );
	}

	@Test
	public void blackLeftMostRank() {
		final String source = "a5";
		final String victim = "b4";

		PositionUtils.addCapturable( position, Side.WHITE, victim );

		testPawn( position, source, Side.BLACK, "a4", victim );
	}

	@Test
	public void blackRightMostRank() {
		final String source = "h6";
		final String victim = "g5";

		PositionUtils.addCapturable( position, Side.WHITE, victim );

		testPawn( position, source, Side.BLACK, "h5", victim );
	}

	@Test
	public void blackInitialPosition() {
		final String source = "d7";
		final String firstVictim = "c6";
		final String secondVictim = "e6";

		PositionUtils.addCapturable( position, Side.WHITE, firstVictim );
		PositionUtils.addCapturable( position, Side.WHITE, secondVictim );

		testPawn( position, source, Side.BLACK, "d6", "d5", firstVictim, secondVictim );
	}

	/**
	 * FIDE 3.7a
	 */
	@Test
	public void singleMoveSecondTry() {
		testPawn( position, "a4", Side.WHITE, "a5" );
	}

	/**
	 * FIDE 3.7e
	 */
	@Test
	public void promotion() {
		testPawn( position, "a7", Side.WHITE, "a8Q", "a8R", "a8N", "a8B" );
	}

	//triangulate in TDD
	@Test
	public void promotionAnotherFile() {
		testPawn( position, "b7", Side.WHITE, "b8Q", "b8R", "b8N", "b8B" );
	}

	@Test
	public void blackPromotion() {
		testPawn( position, "c2", Side.BLACK, "c1Q", "c1R", "c1N", "c1B" );
	}

	@Test
	public void blackPromotionAnotherFile() {
		testPawn( position, "g2", Side.BLACK, "g1Q", "g1R", "g1B", "g1N" );
	}

	@Test
	public void blackPromotionLeftCapture() {
		PositionUtils.addCapturable( position, Side.WHITE, "a1" );
		testPawn( position, "b2", Side.BLACK, "b1Q", "b1R", "b1N", "b1B", "a1Q", "a1N", "a1R", "a1B" );
	}

	@Test
	public void blackPromotionRightCapture() {
		PositionUtils.addCapturable( position, Side.WHITE, "g1" );

		testPawn( position, "f2", Side.BLACK, "f1Q", "f1R", "f1N", "f1B", "g1Q", "g1N", "g1R", "g1B" );
	}

	@Test
	public void promotionWithRightSideCapture() {
		PositionUtils.addCapturable( position, Side.BLACK, "e8" );
		testPawn( position, "d7", Side.WHITE, "d8Q", "d8R", "d8N", "d8B", "e8Q", "e8R", "e8N", "e8B" );
	}

	@Test
	public void promotionWithLeftSideCapture() {
		PositionUtils.addCapturable( position, Side.BLACK, "g8" );

		testPawn( position, "h7", Side.WHITE, "h8Q", "h8R", "h8N", "h8B", "g8Q", "g8N", "g8R", "g8B" );
	}

	//NOTE: In our model it's the largest possible outcome from the position!!!
	//12 different positions are possible by using the pawn!
	@Test
	public void promotionWithBothSideCapture() {
		PositionUtils.addCapturable( position, Side.BLACK, "c8" );
		PositionUtils.addCapturable( position, Side.BLACK, "e8" );

		testPawn( position, "d7", Side.WHITE,
				"d8Q", "d8N", "d8R", "d8B",
				"c8Q", "c8R", "c8N", "c8B",
				"e8Q", "e8N", "e8R", "e8B" );
	}

	@Test
	public void promotionCanCaptureOppositeQueen() {
		position.addQueen( Side.BLACK, "d8" );

		testPawn( position, "e7", Side.WHITE,
				"e8Q", "e8N", "e8R", "e8B",
				"d8Q", "d8N", "d8R", "d8B" );
	}

	@Test
	public void promotionCannotBeDoneWhenBlockedByOurPiece() {
		position.addQueen( Side.WHITE, "e8" );

		testPawn( position, "e7", Side.WHITE );
	}

	@Test
	public void pawnCapturesQueen() {
		position.addQueen( Side.WHITE, "c4" );
		testPawn( position, "d5", Side.BLACK,
				"d4", "c4" );
	}

	@Test
	public void ownPawnBlocksMovingForward() {
		position.addPawn( Side.BLACK, "d5" );
		testPawn( position, "d6", Side.BLACK );
	}

	@Test
	public void ownQueenBlocksMovingForward() {
		position.addQueen( Side.BLACK, "d5" );
		testPawn( position, "d6", Side.BLACK );
	}

	@Test
	public void opponentPawnBlocksMoving() {
		position.addPawn( Side.BLACK, "c7" );
		testPawn( position, "c6", Side.WHITE );
	}

	@Test
	public void doubleMoveImpossibleSecondBusy() {
		position.addPawn( Side.BLACK, "e4" );
		testPawn( position, "e2", Side.WHITE, "e3" );
	}

	@Test
	public void doubleMoveImpossibleFirstBusy() {
		position.addPawn( Side.BLACK, "e3" );
		testPawn( position, "e2", Side.WHITE );
	}

	@Test
	public void doubleMoveBlackImpossibleFirstBusy() {
		position.addPawn( Side.BLACK, "h6" );
		testPawn( position, "h7", Side.BLACK );
	}

	//TODO: while it's not very obvious... it doesn't check if won't capture OUR KING
	//since it won't be created by 'addCapturable'
	//look through other tests to check this
	@Test
	public void promotionCannotCaptureOurPieces() {
		PositionUtils.addCapturable( position, Side.WHITE, "f8" );
		PositionUtils.addCapturable( position, Side.WHITE, "d8" );

		testPawn( position, "e7", Side.WHITE, "e8Q", "e8N", "e8R", "e8B" );
	}

	@Test
	public void promotionCannotGoIfBlockedByOurSide() {
		position.addQueen( Side.WHITE, "a8" );
		testPawn( position, "a7", Side.WHITE );
	}

	@Test
	public void promotionCannotGoIfBlockedByOpponent() {
		position.addQueen( Side.BLACK, "a8" );
		testPawn( position, "a7", Side.WHITE );
	}

}