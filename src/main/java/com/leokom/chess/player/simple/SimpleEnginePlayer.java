package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	private final Logger logger = LogManager.getLogger( this.getClass() );

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
	public void switchToRecodingMode() {
		//TODO: implement if needed
	}

	@Override
	public void opponentMoved( Move opponentMove ) {
		executeMove( opponentMove );
	}

	private void executeMove( Move opponentMove ) {
		moveNumber++;
		logger.info( "Move number = " + moveNumber );
		if ( opponentMove == Move.RESIGN ) {
			logger.info( "Opponent resigned" );
			return;
		}

		//Simplest possible strategy - agree to the draw offer
		if ( opponentMove == Move.OFFER_DRAW ) {
			opponent.opponentMoved( Move.ACCEPT_DRAW );
		}

		switch ( moveNumber ) {
			case 1:
				moveTo( new Move( "e" + rankFrom,  "e" + rankTo ) );
				break;
			case 2:
				moveTo( new Move( "d" + rankFrom, "d" + rankTo ) );
				//NOTE: interesting to implement - how much do we need to wait for result?
				//NOTE2: it's not recommended way to offer draw after the move.
				offerDraw();
				break;
			default:
				resign();
		}
	}

	private void offerDraw() {
		opponent.opponentMoved( Move.OFFER_DRAW );
	}

	private void resign() {
		opponent.opponentMoved( Move.RESIGN );
	}

	/**
	 * Execute the move
	 * @param move move to do
	 */
	private void moveTo( Move move ) {
		//hiding complexity of opponent.opponentMoved call
		opponent.opponentMoved( move );
	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		//TODO: contradicts current understanding of interface
		//of the method
		if ( side == Side.WHITE ) {
			executeMove( null );
		}
	}
}
