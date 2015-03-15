package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;

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
public class MaterialEvaluator implements Evaluator {
	private static final double WORST_MOVE = 0.0;

	@Override
	public double evaluateMove( Position position, Move move ) {
		if ( move.isSpecial() ) {
			return WORST_MOVE;
		}

		final Position target = position.move( move );

		Side ourSide = position.getSideToMove();

		Stream< Piece > ourPieces = target.getPieces( ourSide );
		Stream< Piece > opponentPieces = target.getPieces( ourSide.opposite() );

		int ourMaterialValue = value( ourPieces );
		int opponentMaterialValue = value( opponentPieces );

		int materialAdvantage = ourMaterialValue - opponentMaterialValue;
		return normalizeAdvantage( materialAdvantage );
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
		VALUES.put( PieceType.KING, 1000 );
	}

	static int getValue( PieceType pieceType ) {
		return VALUES.get( pieceType );
	}

	//highly depends on actual values
	//and on fact we evaluate queen higher than other pieces except king
	//and on assumption about 8 max promoted queens

	//no pawns included here since they are 'promoted'
	static final int MAXIMAL_VALUE =
			VALUES.get( PieceType.KING ) +
					9 * VALUES.get( PieceType.QUEEN ) +
					2 * VALUES.get( PieceType.ROOK ) +
					2 * VALUES.get( PieceType.BISHOP ) +
					2 * VALUES.get( PieceType.KNIGHT );

	//technically it should be VALUE(KING)
	//but to support 'invalid' positions we keep the lowest possible value
	private static final int MINIMAL_VALUE = 0;


	private static final int MAXIMAL_ADVANTAGE = MAXIMAL_VALUE - MINIMAL_VALUE;
	private static final int MINIMAL_ADVANTAGE = MINIMAL_VALUE - MAXIMAL_VALUE;

	//convert advantage [ MINIMAL_ADV..MAXIMAL_ADV ] to value [ 0..1 ]
	private static double normalizeAdvantage( int materialAdvantage ) {
		return ( materialAdvantage - MINIMAL_ADVANTAGE ) / (double)
				(MAXIMAL_ADVANTAGE - MINIMAL_ADVANTAGE);
	}

	private static int value( Stream< Piece > pieces ) {
		return pieces
				.map( Piece::getPieceType )
				.mapToInt( VALUES::get )
				.sum();
	}
}
