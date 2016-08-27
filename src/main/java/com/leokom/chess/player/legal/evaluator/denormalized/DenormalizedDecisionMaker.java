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
public class DenormalizedDecisionMaker implements DecisionMaker {
	private static final Logger LOG = LogManager.getLogger();

	private EvaluatorFactory evaluatorFactory = new DenormalizedEvaluatorFactory();

	@Override
	public Optional< Move > findBestMove( Position position ) {
		final Set< Move > legalMoves = position.getMoves();

		if ( legalMoves.isEmpty() ) {
			return Optional.empty();
		}

		//TODO: feel free to select more optimal implementation after profiling,
		//it's the key component of our data structures here
		Table< EvaluatorType, Move, Double > movesTable = HashBasedTable.create();

		Arrays.stream( EvaluatorType.values() )
		.forEach( evaluatorType -> {
			final Evaluator evaluator = evaluatorFactory.get( evaluatorType );
			legalMoves.forEach( move -> movesTable.put( evaluatorType, move, evaluator.evaluateMove( position, move ) ) );
		} );

		movesTable.cellSet().forEach( cell ->
			LOG.debug( "{} [{}] : {}", cell.getColumnKey(), cell.getRowKey(), cell.getValue() )
		);

		return Optional.of( legalMoves.iterator().next() );
	}
}
