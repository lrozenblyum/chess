package com.leokom.chess.player.legal.evaluator.denormalized;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;
import com.leokom.chess.player.legal.evaluator.common.Evaluator;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorFactory;
import com.leokom.chess.player.legal.evaluator.common.EvaluatorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Make decisions based on evaluators from this package
 *
 * Author: Leonid
 * Date-time: 27.08.16 21:51
 */
class DenormalizedDecisionMaker implements DecisionMaker {
	private static final Logger LOG = LogManager.getLogger();
	private static final double DEFAULT_FOR_EQUAL_NOT_IN_RANGE = 0.5;

	private EvaluatorFactory evaluatorFactory = new DenormalizedEvaluatorFactory();

	@Override
	public Optional< Move > findBestMove( Position position ) {
		final Set< Move > legalMoves = position.getMoves();

		if ( legalMoves.isEmpty() ) {
			return Optional.empty();
		}
		Table<EvaluatorType, Move, Double> movesTable = generateInitialTable( position, legalMoves );


		movesTable.cellSet().forEach( cell ->
			LOG.debug( "INITIAL: {} [{}] : {}", cell.getColumnKey(), cell.getRowKey(), cell.getValue() )
		);

		Table<EvaluatorType, Move, Double> normalizedTable = generateNormalized( movesTable );

		normalizedTable.cellSet().forEach( cell ->
			LOG.debug( "NORMALIZED: {} [{}] : {}", cell.getColumnKey(), cell.getRowKey(), cell.getValue() )
		);

		return Optional.of( legalMoves.iterator().next() );
	}

	private Table<EvaluatorType, Move, Double> generateInitialTable( Position position, Set<Move> legalMoves ) {
		//TODO: feel free to select more optimal implementation after profiling,
		//it's the key component of our data structures here
		Table< EvaluatorType, Move, Double > movesTable = HashBasedTable.create();

		Arrays.stream( EvaluatorType.values() )
		.forEach( evaluatorType -> {
			final Evaluator evaluator = evaluatorFactory.get( evaluatorType );
			legalMoves.forEach( move -> movesTable.put( evaluatorType, move, evaluator.evaluateMove( position, move ) ) );
		} );
		return movesTable;
	}

	private Table<EvaluatorType, Move, Double> generateNormalized( Table<EvaluatorType, Move, Double> movesTable ) {
		Table< EvaluatorType, Move, Double > normalizedTable = HashBasedTable.create();

		movesTable.rowKeySet().forEach(
			evaluatorType -> {
				final Collection<Double> values = movesTable.row( evaluatorType ).values();
				double maxValue = Collections.max( values );
				double minValue = Collections.min( values );

				Formula formula;
				if ( maxValue == minValue ) {
					if ( maxValue >= 0.0 && maxValue <= 1.0 ) {
						//ok, all equal but already in normalized range, don't touch?
						formula = this::identityFormula;
					}
					else {
						formula = this::constantMiddleFormula;
					}
				}
				else {
					formula = this::standardizedValueFormula;
				}

				movesTable.row( evaluatorType ).
						forEach( (move, value) ->
								normalizedTable.put( evaluatorType, move, formula.accept( maxValue, minValue, value ) ) );
			}
		);
		return normalizedTable;
	}

	private double identityFormula( double maxValue, double minValue, Double value ) {
		return value;
	}


	private double constantMiddleFormula( double maxValue, double minValue, Double value ) {
		return DEFAULT_FOR_EQUAL_NOT_IN_RANGE;
	}

	private double standardizedValueFormula( double maxValue, double minValue, Double value ) {
		return ( value - minValue ) / ( maxValue - minValue );
	}

	@FunctionalInterface
	private interface Formula {
		double accept( double maxValue, double minValue, Double value );
	}
}
