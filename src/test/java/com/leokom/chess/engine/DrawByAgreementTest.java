package com.leokom.chess.engine;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Leonid
 * Date-time: 15.07.15 21:33
 */
public class DrawByAgreementTest {
	/*
	 * Offer draw :
	 * valid until:
	 * - the opponent accepts it
	 * - rejects it aurally
	 * - rejects it by
touching a piece with the intention of moving or capturing it
     * -  the game is concluded in some other way.
     *
     * For me this describes cases when the opponent can accept draw.
     *
     * TODO:
     * * new position generation
     * The game is drawn upon agreement between the two players during the game.
This immediately ends the game.
	 * * Game state should be different from existing ones!
     * - validity detection
     * ** can accept draw after offer
     * ** cannot accept draw if there is an in-between move from same side?
     *
     * - should we distinguish from WHOM the move is done?
     * some weird engine can push OFFER_DRAW/ACCEPT_DRAW and we silently
     * accept that?
	 */

	@Test
	public void canAcceptAfterDrawOffer() {
		Position position = Position.getInitialPosition();
		final Position newPosition = position.
				move( Move.OFFER_DRAW );
		PositionAsserts.assertAllowedMovesInclude( newPosition, Move.ACCEPT_DRAW );
	}

	@Test
	public void cannotMoveAfterAcceptDraw() {
		Position position = Position.getInitialPosition();
		final Position newPosition = position.
				move( Move.OFFER_DRAW ).
				move( Move.ACCEPT_DRAW );
		assertTrue( newPosition.isTerminal() );
	}

	@Test
	public void noSideToMoveInTerminal() {
		Position position = Position.getInitialPosition();
		final Position newPosition = position.
				move( Move.OFFER_DRAW ).
				move( Move.ACCEPT_DRAW );
		assertNull( newPosition.getSideToMove() );
	}

	@Test
	public void noWinningSideInDraw() {
		Position position = Position.getInitialPosition();
		final Position newPosition = position.
				move( Move.OFFER_DRAW ).
				move( Move.ACCEPT_DRAW );
		assertNull( newPosition.getWinningSide() );
	}

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

	@Test
	public void prohibitedForTerminal() {
		Position position = Position.getInitialPosition();
		PositionAsserts.assertAllowedMovesOmit( position.move( Move.RESIGN ),
				Move.OFFER_DRAW );
	}
}
