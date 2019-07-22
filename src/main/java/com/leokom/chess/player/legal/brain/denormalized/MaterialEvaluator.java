package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.SideEvaluator;
import com.leokom.chess.player.legal.brain.internal.common.SymmetricEvaluator;

import java.util.stream.Stream;

import static com.leokom.chess.player.legal.brain.internal.common.MaterialValues.VALUES;

/**
 * Evaluate material domination
 *
 * This is the first brain
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
class MaterialEvaluator implements Evaluator {
	/**
	 *
	 * {@inheritDoc}
	 * @return sum of values of pieces on board - sum of values of opponent's pieces on board
	 * thus in boundaries (- max. sum of value of pieces of a side, max. sum of value of pieces of a side)
	 * (exclusive because 0 of a side is not an option)
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );
		return new SymmetricEvaluator( new MaterialSideEvaluator() ).evaluate( target );
	}

	private static class MaterialSideEvaluator implements SideEvaluator {

		@Override
		public double evaluatePosition(Position position, Side side) {
			return value( position.getPieces( side ).
					//king is excluded because it's invaluable
							filter( this::isNotAKing ) );
		}

		private boolean isNotAKing( Piece piece ) {
			return piece.getPieceType() != PieceType.KING;
		}

		private static int value( Stream< Piece > pieces ) {
			return pieces
					.map( Piece::getPieceType )
					.mapToInt( VALUES::get )
					.sum();
		}
	}

	static int getValue( PieceType pieceType ) {
		return VALUES.get( pieceType );
	}
}
