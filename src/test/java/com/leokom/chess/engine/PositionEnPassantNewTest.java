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
}