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
	private Side side;
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
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		getLogger().info( "Opponent suggested me started a new game whites. Starting it" );
		side = Side.WHITE;
		position = Position.getInitialPosition();
		executeMove();
	}

	@Override
	public void opponentMoved( String opponentMove ) {
		//REFACTOR: should be part of man-in-the-middle (judge, board, validator?)
		if ( opponentMove == null ) {
			throw new IllegalArgumentException( "Wrong opponent move null" );
		}

		//updating internal representation of our position according to the opponent's move
		updatePositionByOpponentMove( opponentMove );

		executeMove();
	}

	private void updatePositionByOpponentMove( String opponentMove ) {
		String source = opponentMove.substring( 0, 2 );
		String destination = opponentMove.substring( 2 );

		position = position.move( new Move( source, destination ) );
	}

	//exposing package-private for tests
	void executeMove() {
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
		opponent.opponentMoved( move.toOldStringPresentation() );
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
