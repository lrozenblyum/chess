package com.leokom.chess.player.legal.brain.denormalized;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.brain.common.*;
import com.leokom.chess.player.legal.brain.internal.common.EvaluatorWeights;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Make decisions based on evaluators from this package
 *
 * Author: Leonid
 * Date-time: 27.08.16 21:51
 */
public class DenormalizedBrain implements Brain {
	private static final Logger LOG = LogManager.getLogger();
	private static final double DEFAULT_FOR_EQUAL_NOT_IN_RANGE = 0.5;

	private EvaluatorFactory evaluatorFactory = new DenormalizedEvaluatorFactory();

	@Override
	public List< Move > findBestMove( Position position ) {
		final Set< Move > legalMoves = position.getMoves();

		if ( legalMoves.isEmpty() ) {
			return Collections.emptyList();
		}

		Table<EvaluatorType, Move, Double> movesTable = generateInitialTable( position, legalMoves );
		logTable( movesTable, "INITIAL" );

		Table<EvaluatorType, Move, Double> normalizedTable = generateNormalized( movesTable );
		logTable( normalizedTable, "NORMALIZED" );

		Table<EvaluatorType, Move, Double> weightedTable = generateWithWeights( normalizedTable );
		logTable( weightedTable, "WEIGHTED" );

		return new NormalizedBrain<>( getEvaluator( weightedTable ) ).findBestMove( position );
	}

	private Evaluator getEvaluator(Table<EvaluatorType, Move, Double> weightedTable) {
		return ( position, move ) ->
            //summing without converting to DoubleStream http://stackoverflow.com/q/24421140
            weightedTable.column( move ).values().stream().reduce( 0.0, Double::sum );
	}

	private void logTable( Table<EvaluatorType, Move, Double> weightedTable, String prefix ) {
		if ( LOG.isTraceEnabled() ) {
			weightedTable.cellSet().forEach(cell -> LOG.debug(prefix + ": {} [{}] : {}", cell.getColumnKey(), cell.getRowKey(), cell.getValue()));
		}
	}

	private Table<EvaluatorType, Move, Double> generateWithWeights( Table<EvaluatorType, Move, Double> normalizedTable ) {
		final Map<EvaluatorType, Double> standardWeights = new EvaluatorWeights().asMap();

		Table< EvaluatorType, Move, Double > result = HashBasedTable.create();

		//TODO: if one of normalized evaluators is disabled, here we'll get NPE.
		normalizedTable.cellSet().forEach( cell ->
			result.put( cell.getRowKey(), cell.getColumnKey(), cell.getValue() * standardWeights.get( cell.getRowKey() ) )
		);

		return result;
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

				Formula formula = getFormula( minValue, maxValue );

				movesTable.row( evaluatorType ).
						forEach( (move, value) ->
								normalizedTable.put( evaluatorType, move, formula.accept( maxValue, minValue, value ) ) );
			}
		);
		return normalizedTable;
	}

	private Formula getFormula( double minValue, double maxValue ) {
		if ( maxValue != minValue ) {
			//TODO: technically, if all values are in [ 0, 1 ] range we may not normalize
			//is it wise?
			return this::standardizedValueFormula;
		}

		if ( maxValue >= 0.0 && maxValue <= 1.0 ) {
		//ok, all equal but already in normalized range, don't touch?
			return this::identityFormula;
		}
		else {
			return this::constantMiddleFormula;
		}
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
