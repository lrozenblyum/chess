package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 08.03.15 21:15
 */
public class ResignTest {
	@Test
	public void noMoreMovesAfterResign() {
		Position position = Position.getInitialPosition();

		final Position positionAfterResign = position.move( Move.RESIGN );
		//implies 'no resign after resign'
		assertTrue( positionAfterResign.getMoves().isEmpty() );
	}

	@Test
	public void positionAfterResignKeepsPieces() {
		Position position = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.KING )
				.add( Side.BLACK, "h7", PieceType.QUEEN )
				.add( Side.BLACK, "h8", PieceType.KING )
				.setSideOf( "a1" )
				.build();

		Position positionAfterResign = position.move( Move.RESIGN );
		assertTrue( positionAfterResign.hasPiece( Side.WHITE, "a1", PieceType.KING ) );
	}

	@Test
	public void winningSideDetection() {
		final Position afterResign = Position.getInitialPosition().move( Move.RESIGN );
		assertEquals( Side.BLACK, afterResign.getWinningSide() );
	}

	@Test
	public void winningSideDetectionOpposite() {
		final Position afterResign = Position.getInitialPosition()
				.move( new Move( "e2", "e4" ) )
				.move( Move.RESIGN );
		assertEquals( Side.WHITE, afterResign.getWinningSide() );
	}

	@Test
	public void resignNotEqualsToOtherMove() {
		assertNotEquals( Move.RESIGN, new Move( "e2", "e4" ) );
	}

	@Test
	public void resignEqualsToItself() {
		assertEquals( Move.RESIGN, Move.RESIGN );
	}

	@Test
	public void resignIsPossibleForNonTerminal() {
		Position position = Position.getInitialPosition();
		PositionAsserts.assertAllowedMovesInclude( position, Move.RESIGN );
	}

	@Test
	public void noResignAfterCheckmate() {
		Position position =
			Position.getInitialPosition()
				.move( "f2", "f3" )
				.move( "e7", "e5" )
				.move( "g2", "g4" )
				.move( "d8", "h4" );
		assertTrue( position.getMoves().isEmpty() );
	}

	/**
	 * Backlog
	 * Generator
	 * + correct winning side detection
	 * + correct equals in Move
	 * + correct toString?
	 *
	 * Allowed moves detection:
	 * + add it to allowed moves
	 * + only if non-terminal
	 */
}
