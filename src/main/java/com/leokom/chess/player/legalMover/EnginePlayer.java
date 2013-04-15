package com.leokom.chess.player.legalMover;

import com.leokom.chess.MainRunner;
import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

/**
* Author: Leonid
* Date-time: 15.04.13 22:26
*/
public class EnginePlayer implements Player {
	//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
	private int moveNumber;
	private Player opponent;
	private final Logger logger = Logger.getLogger( this.getClass() );

	public EnginePlayer() {
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
		logger.info( "Detected allowance to go. Move number = " + moveNumber );
		switch ( moveNumber ) {
			case 1:
				opponent.opponentMoved( "e2e4" );
				break;
			case 2:
				opponent.opponentMoved( "d2d4" );
				//NOTE: interesting to implement - how much do we need to wait for result?
				//NOTE2: it's not recommended way to offer draw after the move.
				opponent.opponentOfferedDraw();
				break;
			default:
				opponent.opponentResigned();
		}
	}

	@Override
	public void run() {
		throw new UnsupportedOperationException( "Definitely it's a sign 'run' must be removed from this interface" );
	}

	@Override
	public void opponentAgreedToDrawOffer() {
		//To change body of implemented methods use File | Settings | File Templates.
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
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
