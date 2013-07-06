package com.leokom.chess.engine;

import org.junit.Test;

import static com.leokom.chess.engine.PositionAsserts.*;
import static com.leokom.chess.engine.Side.*;

/**
 * Author: Leonid
 * Date-time: 28.09.12 22:09
 */
public class PawnPromoteNewPositionTest {
	@Test
	public void toQueen() {
		Position position = new Position( null );
		position.addPawn( WHITE, "c7" );

		Position newPosition = position.move( "c7", "c8Q" );
		assertEmptySquare( newPosition, "c7" );
		assertHasPiece( newPosition, PieceType.QUEEN, Side.WHITE, "c8" );
	}

	@Test
	public void toQueenAnotherFile(){
		Position position = new Position( null ); //any en passant...
		position.addPawn( WHITE, "a7" );

		Position newPosition = position.move( "a7", "a8Q" );
		assertEmptySquare( newPosition, "a7" );
		assertHasPiece( newPosition, PieceType.QUEEN, WHITE, "a8" );

	}

	@Test
	public void toQueenBlack() {
		Position position = new Position( null ); //any en passant...
		position.addPawn( BLACK, "b2" );

		Position newPosition = position.move( "b2", "b1Q" );
		assertEmptySquare( newPosition, "b2" );
		assertHasPiece( newPosition, PieceType.QUEEN, BLACK, "b1" );
		assertHasNoPawn( newPosition, "b1", BLACK );
	}

	@Test
	public void captureLeftSide() {
		Position position = new Position( null );
		position.addPawn( Side.WHITE, "h7" );

		//the only piece I can capture of the opposite side on the 8'th rank
		//is queen... pawn cannot exist there. other pieces haven't been introduced
		//into the game yet
		position.addQueen( Side.BLACK, "g8" );

		Position newPosition = position.move( "h7", "g8Q" );

		assertEmptySquare( newPosition, "h7" );
		assertHasPiece( newPosition, PieceType.QUEEN, WHITE, "g8" );
		assertHasNoPawn( newPosition, "g8", WHITE );
		assertEmptySquare( newPosition, "h8" );
	}

	@Test
	public void promotionLeavesRestOfPositionUntouched() {
		Position position = new Position( null );
		position.addPawn( WHITE, "a7" );
		position.addPawn( WHITE, "c5" );
		position.addQueen( BLACK, "h4" );
		position.addPawn( BLACK, "d5" );

		Position newPosition = position.move( "a7", "a8Q" );

		assertHasPawn( newPosition, "d5", BLACK );
		assertHasPiece( newPosition, PieceType.QUEEN, BLACK, "h4" );
		assertHasPawn( newPosition, "c5", WHITE );
	}

	@Test
	public void promoteToKnight() {
		Position position = new Position( null );
		position.addPawn( WHITE, "a7" );

		Position newPosition = position.move( "a7", "a8N" );
		assertEmptySquare( newPosition, "a7" );
		assertHasPiece( newPosition, PieceType.KNIGHT, Side.WHITE, "a8" );
	}


	@Test
	public void promoteToRook() {
		Position position = new Position( null );
		position.addPawn( BLACK, "g2" );

		Position newPosition = position.move( "g2", "g1R" );
		assertEmptySquare( newPosition, "g2" );
		assertHasPiece( newPosition, PieceType.ROOK, Side.BLACK, "g1" );
	}

	@Test
	public void promoteToBishop() {
		Position position = new Position( null );
		position.addPawn( BLACK, "c2" );

		Position newPosition = position.move( "c2", "c1B" );
		assertEmptySquare( newPosition, "c2" );
		assertHasPiece( newPosition, PieceType.BISHOP, Side.BLACK, "c1" );
	}

	@Test
	public void promoteWithCaptureToRook() {
		Position position = new Position( null );
		position.addPawn( WHITE, "g7" );
		position.add( BLACK, "h8", PieceType.KNIGHT );

		Position newPosition = position.move( "g7", "h8R" );
		assertEmptySquare( newPosition, "g7" );
		assertHasPiece( newPosition, PieceType.ROOK, Side.WHITE, "h8" );
	}
}
