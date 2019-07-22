package com.leokom.chess.player.legal.brain.denormalized;

import com.google.common.collect.Sets;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Evaluator;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
class AttackEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Side ourSide = position.getSideToMove();
		Position targetPosition = position.move(move);
		return getIndex( targetPosition, ourSide )
				- getIndex( targetPosition, ourSide.opposite() );
	}

	/*
	 * Backlog for improvements:
	 * - king has become a main target for attacks
	 */
	private float getIndex( Position targetPosition, Side side ) {
		final Set< String > squaresAttacked = getPiecesAttackedBy( targetPosition, side );

		// sum of piece values
		// if a piece is protected - index of piece value is reduced
		float result = 0;
		for ( String attackedSquare : squaresAttacked ) {
			//REFACTOR: probably bad dependency on another brain - extract common utility
			int pieceValue = MaterialEvaluator.getValue( targetPosition.getPieceType( attackedSquare ) );

			final Stream< String > protectors = targetPosition.getSquaresAttackingSquare( side.opposite(), attackedSquare );

			//+1 to avoid / 0, more protectors is better
			result += pieceValue / ( protectors.count() + 1.0 );
		}
		return result;
	}

	//TODO: technically, in case of a terminal position, it should return empty result
	//REFACTOR: too generic to encapsulate into Position?
	private static Set<String> getPiecesAttackedBy( Position position, Side attackerSide ) {
		Set<String> defenderSquares = position.getSquaresOccupiedBySide( attackerSide.opposite() );
		return Sets.intersection( position.getSquaresAttackedBy( attackerSide ),
				defenderSquares );
	}
}
