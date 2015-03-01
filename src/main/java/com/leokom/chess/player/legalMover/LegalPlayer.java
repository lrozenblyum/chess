package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private Player opponent;
	private Position position = Position.getInitialPosition();

	/**
	 * Create player
	 */
	public LegalPlayer() {
	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		getLogger().info( "Opponent suggested me started a new game whites. Starting it" );
		position = Position.getInitialPosition();
		executeMove();
	}

	@Override
	public void opponentMoved( Move opponentMove ) {
		//REFACTOR: should be part of man-in-the-middle (judge, board, validator?)
		if ( opponentMove == null ) {
			throw new IllegalArgumentException( "Wrong opponent move null" );
		}

		//updating internal representation of our position according to the opponent's move
		updatePositionByOpponentMove( opponentMove );

		executeMove();
	}

	private void updatePositionByOpponentMove( Move opponentMove ) {
		position = position.move( opponentMove );
	}

	//exposing package-private for tests
	void executeMove() {
		Set< Move > legalMoves = position.getMoves();

		if ( !legalMoves.isEmpty() ) {
			Move move = findBestMove( legalMoves );

			updateInternalPositionPresentation( move );

			informOpponentAboutTheMove( move );
		}
		else {
			getLogger().info( "We cannot execute any moves." +
					" Final state has been detected." +
					" Result will be based on Position information " );
		}
	}

	private void informOpponentAboutTheMove( Move move ) {
		opponent.opponentMoved( move );
	}

	//updating internal representation of current position according to our move
	private void updateInternalPositionPresentation( Move move ) {
		getLogger().info( this.position.getSideToMove() + " : Moved " + move );
		position = position.move( move );
		getLogger().info( "\nNew position : " + position );
	}

	/**
	 *
	 * @param legalMoves not-empty set of moves
	 * @return best move according to current strategy
	 */
	private Move findBestMove( Set< Move > legalMoves ) {
		Evaluator brains = new MasterEvaluator();

		Map< Move, Double > moveRatings = new HashMap<>();
		for ( Move move : legalMoves ) {
			moveRatings.put( move, brains.evaluateMove( position, move ) );
		}

		return getMoveWithMaxRating( moveRatings );
	}


	private Move getMoveWithMaxRating( Map< Move, Double > moveValues ) {
		double maxValue = ( Collections.max( moveValues.values() ));

		for ( Map.Entry< Move, Double > entry: moveValues.entrySet() ) {
			if ( entry.getValue() == maxValue ) {
				return entry.getKey();
			}
		}

	 	throw new AssertionError( "Since map is not empty we mustn't come here" );
	}

	private Logger getLogger() {
		return LogManager.getLogger( this.getClass() );
	}

	@Override
	public void opponentResigned() {

	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	//injecting the position for tests, however maybe in future
	//it's useful for starting game from a non-initial position
	void setPosition( Position position ) {
		this.position = position;
	}
}
