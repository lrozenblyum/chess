package com.leokom.chess.player.legal.evaluator.denormalized;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Evaluate material domination
 *
 * This is the first evaluator
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
class MaterialEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	/**
	 *
	 * {@inheritDoc}
	 * @return [ -big value, big value ]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( move.isSpecial() ) {
			//FIXME: check ALL denormalized evaluators for correctness of this value
			return WORST_MOVE;
		}

		final Position target = position.move( move );

		Side ourSide = position.getSideToMove();

		int ourMaterialValue = getMaterialValue( target, ourSide );
		int opponentMaterialValue = getMaterialValue( target, ourSide.opposite() );

		return ourMaterialValue - opponentMaterialValue;
	}

	private int getMaterialValue( Position position, Side side ) {
		return value( position.getPieces( side ).
			//king is excluded because it's invaluable
			filter( this::isNotAKing ) );
	}

	private boolean isNotAKing( Piece piece ) {
		return piece.getPieceType() != PieceType.KING;
	}

	private static final Map< PieceType,Integer > VALUES = new
			HashMap<>();
	//heuristic, may be dynamic depending on situation on the board!
	static {
		VALUES.put( PieceType.PAWN, 1 );
		VALUES.put( PieceType.KNIGHT, 3 );
		VALUES.put( PieceType.BISHOP, 3 );
		VALUES.put( PieceType.ROOK, 5 );
		VALUES.put( PieceType.QUEEN, 9 );
		//practically King is invaluable,
		//however for sum purposes like attackIndex
		// we need some value associated
		VALUES.put( PieceType.KING, 1000 );
	}

	static int getValue( PieceType pieceType ) {
		return VALUES.get( pieceType );
	}

	private static int value( Stream< Piece > pieces ) {
		return pieces
				.map( Piece::getPieceType )
				.mapToInt( VALUES::get )
				.sum();
	}
}
