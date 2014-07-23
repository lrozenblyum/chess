package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private final Side side;
	private Player opponent;
	private Position position = Position.getInitialPosition();

	/**
	 * Create player that will play for the side
	 * @param side who we play for?
	 */
	public LegalPlayer( Side side ) {
		this.side = side;
	}

	@Override
	public void run() {
		//if we are white -> enforce moving!
		if ( side == Side.WHITE ) {
			opponentMoved( null );
		}
	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentMoved( String opponentMove ) {
		//TODO: null is a hidden ugly way to say 'it's our first move now'
		if ( opponentMove != null ) {
			String source = opponentMove.substring( 0, 2 );
			String destination = opponentMove.substring( 2 );

			//updating internal representation of our position according to the opponent's move
			position = position.move( new Move( source, destination ) );
		}

		Set< Move > legalMoves = position.getMoves( side );

		if ( !legalMoves.isEmpty() ) {
			Move move = findBestMove( legalMoves );

			updateInternalPositionPresentation( move );

			informOpponentAboutTheMove( move );
		}
		else {
			getLogger().info( "Final state detected" );
			//TODO: if empty set it means the game has been finished (what's the result?)
		}
	}

	private void informOpponentAboutTheMove( Move move ) {
		opponent.opponentMoved( move.getFrom() + move.getTo() );
	}

	//updating internal representation of current position according to our move
	private void updateInternalPositionPresentation( Move move ) {
		position = position.move( move );
		getLogger().info( this.side + " : Moved " + move + "\nNew position : " + position );
	}

	/**
	 *
	 * @param legalMoves not-empty set of moves
	 * @return best move according to current strategy
	 */
	private Move findBestMove( Set< Move > legalMoves ) {
		Map< Move, Double > moveRatings = new HashMap<>();
		for ( Move move : legalMoves ) {
			moveRatings.put( move, evaluateMove( move ) );
		}

		return getMoveWithMaxRating( moveRatings );
	}

	private double evaluateMove( Move move ) {
		//we don't need to know that we can execute other moves
		//while evaluating a move, do we?
		//so far no, but from human logic we need that possibility
		//among 2 'equal' moves we would like to select according to some
		//compare 1-to-another logic
		List< Evaluator > evaluators = Arrays.asList( new CastlingSafetyEvaluator(), new CenterControlEvaluator(), new MobilityEvaluator() );
		double estimate = 0;
		for ( Evaluator evaluator : evaluators ) {
			// rather safe if all evaluators keep convention [0 , 1]
			//here we also can multiply by weight of each evaluator for us
			estimate += evaluator.evaluateMove( position, move );
		}

		return estimate;
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
		return Logger.getLogger( this.getClass() );
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
