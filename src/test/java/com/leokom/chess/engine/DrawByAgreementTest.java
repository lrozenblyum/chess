package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 15.07.15 21:33
 */
public class DrawByAgreementTest {
	@Test
	public void canMoveAfterOfferDraw() {
		Position position = Position.getInitialPosition();

		final Position positionAfterResign = position.move( Move.OFFER_DRAW );

		assertFalse( positionAfterResign.getMoves().isEmpty() );
	}

	@Test
	public void correctRightToMove() {
		Position position = Position.getInitialPosition();

		final Position positionAfterResign = position.move( Move.OFFER_DRAW );

		assertEquals( Side.WHITE, positionAfterResign.getSideToMove() );
	}

	@Test
	public void notTerminalPosition() {
		Position position = Position.getInitialPosition();

		final Position positionAfterResign = position.move( Move.OFFER_DRAW );
		assertFalse( positionAfterResign.isTerminal() );
	}

	@Test
	public void allowedForNonTerminal() {
		Position position = Position.getInitialPosition();
		PositionAsserts.assertAllowedMovesInclude( position, Move.OFFER_DRAW );
	}
}
