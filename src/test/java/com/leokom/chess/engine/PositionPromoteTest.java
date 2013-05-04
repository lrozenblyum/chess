package com.leokom.chess.engine;

import org.junit.Test;

import static com.leokom.chess.engine.PositionAsserts.assertEmptySquare;
import static com.leokom.chess.engine.PositionAsserts.assertHasPiece;
import static com.leokom.chess.engine.Side.*;

/**
 * Author: Leonid
 * Date-time: 28.09.12 22:09
 */
public class PositionPromoteTest {
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
	}
}
