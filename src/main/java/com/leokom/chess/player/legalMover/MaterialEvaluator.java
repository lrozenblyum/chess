package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Evaluate material domination
 *
 * This is the first evaluator
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
public class MaterialEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		final Position target = position.move( move );

		Side ourSide = position.getSide( move.getFrom() );

		List< Piece > ourPieces = target.getPieces( ourSide );
		List< Piece > opponentPieces = target.getPieces( ourSide.opposite() );

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

	//highly depends on actual values
	//and on fact we evaluate queen higher than other pieces except king
	//and on assumption about 8 max promoted queens

	//no pawns included here since they are 'promoted'
	private static final int MAXIMAL_VALUE =
			VALUES.get( PieceType.KING ) +
					8 * VALUES.get( PieceType.QUEEN ) +
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

	private static int value( List< Piece > pieces ) {
		return pieces.stream()
				.mapToInt( piece -> VALUES.get( piece.getPieceType() ) )
				.sum();
	}
}
