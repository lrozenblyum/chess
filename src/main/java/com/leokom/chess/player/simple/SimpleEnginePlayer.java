package com.leokom.chess.player.simple;

import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

/**
 * Run just 2 moves for white
 * Always agree to draw.
 * Resign on the 3'd move
 *
* Author: Leonid
* Date-time: 15.04.13 22:26
*/
public class SimpleEnginePlayer implements Player {
	//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
	private int moveNumber;
	private Player opponent;
	private final Logger logger = Logger.getLogger( this.getClass() );

	public SimpleEnginePlayer() {
		moveNumber = 0;
	}

	//TODO: asymmetric setter to have possibility one player to another
	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void opponentMoved( String opponentMove ) {
		moveNumber++;
		logger.info( "Move number = " + moveNumber );
		switch ( moveNumber ) {
			case 1:
				moveTo( "e2e4" );
				break;
			case 2:
				moveTo( "d2d4" );
				//NOTE: interesting to implement - how much do we need to wait for result?
				//NOTE2: it's not recommended way to offer draw after the move.
				offerDraw();
				break;
			default:
				resign();
		}
	}

	private void offerDraw() {
		opponent.opponentOfferedDraw();
	}

	private void resign() {
		opponent.opponentResigned();
	}

	/**
	 * Execute the move
	 * @param move move in some supported notation (TO BE DEFINED)
	 */
	private void moveTo( String move ) {
		//hiding complexity of opponent.opponentMoved call
		opponent.opponentMoved( move );
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException( "Definitely it's a sign 'run' must be removed from this interface" );
	}

	@Override
	public void opponentAgreedToDrawOffer() {
		logger.info( "Opponent agreed to draw offer" );
	}

	/**
	 * Simplest possible strategy - agree to the draw offer
	 */
	@Override
	public void opponentOfferedDraw() {
		opponent.opponentAgreedToDrawOffer();
	}

	@Override
	public void opponentResigned() {
		logger.info( "Opponent resigned" );
	}
}
