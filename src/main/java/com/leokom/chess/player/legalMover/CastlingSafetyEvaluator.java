package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

import java.util.HashSet;
import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 14.07.14 22:58
 */
class CastlingSafetyEvaluator implements Evaluator {
	//we can also create some enum and use it instead of integers in data structures
	//however so far it's simpler to have in so to reuse Collections.max etc
	private static final double GOOD_MOVE = 1;
	private static final double ACCEPTABLE_MOVE = 0.5;
	private static final double BAD_MOVE = 0;

	/*
	 * TODO: backlog
	 * If rook is captured - don't think it would be possible
	 * castling with it
	 *
	 * Moving a piece out between king & rook is good
	 *
	 * Moving a piece out between king & rook is even better
	 * if it immediately gives space for castling
	 *
	 * Moving 1 rook is not so harmful if second one hasn't yet moved
	 *
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		//if king has moved already - all other moves are fine
		//they don't bring anything for castling safety
		final Side side = position.getSideToMove();

		if ( position.hasKingMoved( side ) ) {
			return ACCEPTABLE_MOVE;
		}

		//both rooks moved - no chance to castling
		if ( position.hasARookMoved( side ) && position.hasHRookMoved( side ) ) {
			return ACCEPTABLE_MOVE;
		}

		//strategy : 'castling addicted player'
		// avoid moving rook and king
		//if it's not castling (I want to see castling)
		//in principle after castling we could allow such moves

		double moveWeight = ACCEPTABLE_MOVE;

		if ( position.getPieceType( move.getFrom() ) == PieceType.ROOK ) {
			moveWeight = BAD_MOVE;
		}

		Set< Move > castlingMoves = new HashSet< Move >() {
			{
				add( new Move( "e1", "g1" ) );
				add( new Move( "e1", "c1" ) );
				add( new Move( "e8", "g8" ) );
				add( new Move( "e8", "c8" ) );
			}
		};
		if ( position.getPieceType( move.getFrom() ) == PieceType.KING ) {
			//REFACTOR: duplication with PositionGenerator to detect castling moves

			moveWeight = castlingMoves.contains( move ) ? GOOD_MOVE : BAD_MOVE;
		}
		return moveWeight;
	}
}
