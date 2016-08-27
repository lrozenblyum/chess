package com.leokom.chess.player.legal;

import com.google.common.collect.Sets;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Helper to estimate position attack situation
 * useful both for attack and protection evaluators
 * Author: Leonid
 * Date-time: 25.08.16 21:11
 */
final class AttackIndexCalculator {
	private AttackIndexCalculator() {}

	/*
	 * Backlog for improvements:
	 * - for protection evaluator king is taken too seriously
	 * - for attack evaluator king has become a main target for attacks
	 */

	static float getAttackIndex( Position targetPosition, Side attackerSide ) {
		final Set< String > squaresAttacked = getPiecesAttackedBy( targetPosition, attackerSide );

		// sum of piece values
		// if a piece is protected - index of piece value is reduced
		float result = 0;
		for ( String attackedSquare : squaresAttacked ) {
			//REFACTOR: probably bad dependency on another evaluator - extract common utility
			int pieceValue = MaterialEvaluator.getValue( targetPosition.getPieceType( attackedSquare ) );

			final Stream< String > protectors = targetPosition.getSquaresAttackingSquare( attackerSide.opposite(), attackedSquare );

			//+1 to avoid / 0, more protectors is better
			result += pieceValue / ( protectors.count() + 1.0 );
		}
		return result;
	}

	//REFACTOR: too generic to encapsulate into Position?
	private static Set<String> getPiecesAttackedBy( Position position, Side attackerSide ) {
		Set<String> defenderSquares = position.getSquaresOccupiedBySide( attackerSide.opposite() );
		return Sets.intersection( position.getSquaresAttackedBy( attackerSide ),
			defenderSquares );
	}
}
