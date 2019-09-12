package com.leokom.games.chess.player.legal.brain.denormalized;

import com.google.common.collect.Sets;
import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.legal.brain.common.Evaluator;
import com.leokom.games.chess.player.legal.brain.internal.common.SymmetricEvaluator;
import com.leokom.games.chess.player.legal.brain.common.SideEvaluator;

import java.util.Set;
import java.util.stream.Stream;

/**
 * Author: Leonid
 * Date-time: 25.08.16 20:54
 */
class AttackEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		Position targetPosition = position.move(move);
		return new SymmetricEvaluator( new AttackSideEvaluator() ).evaluate( targetPosition );
	}

	private static class AttackSideEvaluator implements SideEvaluator {
		//TODO: technically, in case of a terminal position, it should return empty result
		//REFACTOR: too generic to encapsulate into Position?
		private static Set<String> getPiecesAttackedBy( Position position, Side attackerSide ) {
			Set<String> defenderSquares = position.getSquaresOccupiedBySide( attackerSide.opposite() );
			return Sets.intersection( position.getSquaresAttackedBy( attackerSide ),
					defenderSquares );
		}

		/*
		 * Backlog for improvements:
		 * - king has become a main target for attacks
		 */
		@Override
		public double evaluatePosition(Position position, Side side) {
			final Set< String > squaresAttacked = getPiecesAttackedBy( position, side );

			// sum of piece values
			// if a piece is protected - index of piece value is reduced
			float result = 0;
			for ( String attackedSquare : squaresAttacked ) {
				//REFACTOR: probably bad dependency on another brain - extract common utility
				int pieceValue = MaterialEvaluator.getValue( position.getPieceType( attackedSquare ) );

				final Stream< String > protectors = position.getSquaresAttackingSquare( side.opposite(), attackedSquare );

				//+1 to avoid / 0, more protectors is better
				result += pieceValue / ( protectors.count() + 1.0 );
			}
			return result;
		}
	}

}
