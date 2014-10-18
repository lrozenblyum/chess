package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

/**
 * Run just 2 moves for white/black (central pawns)
 * Always agree to draw.
 * Resign on the 3'd move
 *
* Author: Leonid
* Date-time: 15.04.13 22:26
*/
public class SimpleEnginePlayer implements Player {
	private final Side side;
	//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
	private int moveNumber;
	private Player opponent;
	private final Logger logger = Logger.getLogger( this.getClass() );

	private final int rankFrom;
	private final int rankTo;

	public SimpleEnginePlayer( Side side ) {
		moveNumber = 0;

		this.side = side;
		rankFrom = side == Side.WHITE ? 2 : 7;
		rankTo = side == Side.WHITE ? 4 : 5;
	}

	//TODO: asymmetric setter to have possibility one player to another
	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void opponentMoved( String opponentMove ) {
		executeMove();
	}

	private void executeMove() {
		moveNumber++;
		logger.info( "Move number = " + moveNumber );
		switch ( moveNumber ) {
			case 1:
				moveTo( "e" + rankFrom + "e" + rankTo );
				break;
			case 2:
				moveTo( "d" + rankFrom + "d" + rankTo );
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
	public void opponentAgreedToDrawOffer() {
		logger.info( "Opponent agreed to draw offer" );
	}

	@Override
	public void opponentSuggestsMeStartGame() {
		//TODO: contradicts current understanding of interface
		//of the method
		if ( side == Side.WHITE ) {
			executeMove();
		}
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
