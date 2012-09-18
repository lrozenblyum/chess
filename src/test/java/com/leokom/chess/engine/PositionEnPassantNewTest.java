package com.leokom.chess.engine;

import org.junit.Test;
import static com.leokom.chess.engine.PositionAsserts.*;

/**
 * Author: Leonid
 * Date-time: 18.09.12 21:50
 */
public class PositionEnPassantNewTest {
	@Test
	public void enPassantLeft() {
		Position position = new Position( "d" );
		position.addPawn( Side.BLACK, "d5" );

		position.addPawn( Side.WHITE, "e5" );

		Position newPosition = position.move( "e5", "d6" );

		assertEmptySquare( newPosition, "e5" );
		assertHasPawn( newPosition, "d6", Side.WHITE );
		assertEmptySquare( newPosition, "d5" );
	}

	@Test
	public void enPassantLeftTriangle() {
		Position position = new Position( "e" );
		position.addPawn( Side.BLACK, "e5" );

		position.addPawn( Side.WHITE, "f5" );

		Position newPosition = position.move( "f5", "e6" );

		assertEmptySquare( newPosition, "f5" );
		assertHasPawn( newPosition, "e6", Side.WHITE );
		assertEmptySquare( newPosition, "e5" );
	}
}