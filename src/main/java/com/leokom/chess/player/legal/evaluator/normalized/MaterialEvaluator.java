package com.leokom.chess.player.legal.evaluator.normalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import com.leokom.chess.player.legal.evaluator.denormalized.DenormalizedEvaluatorFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Evaluate material domination
 *
 * This is the first evaluator
 * for which I think symmetric counter-part
 * has no sense (since it's a difference between us & opponent)
 *
 */
class MaterialEvaluator implements Evaluator {
	@Override
	public double evaluateMove( Position position, Move move ) {
		double materialAdvantage = new DenormalizedEvaluatorFactory().get( EvaluatorType.MATERIAL ).evaluateMove( position, move );
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
		//practically King is invaluable,
		//however for sum purposes like attackIndex
		// we need some value associated
		VALUES.put( PieceType.KING, 1000 );
	}

	//highly depends on actual values
	//and on fact we evaluate queen higher than other pieces except king
	//and on assumption about 8 max promoted queens

	//no pawns included here since they are 'promoted'
	static final int MAXIMAL_VALUE =
					VALUES.get( PieceType.KING ) + //TODO: needed for external but not for internal usage
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
	private static double normalizeAdvantage( double materialAdvantage ) {
		return ( materialAdvantage - MINIMAL_ADVANTAGE ) / (double)
				(MAXIMAL_ADVANTAGE - MINIMAL_ADVANTAGE);
	}
}
