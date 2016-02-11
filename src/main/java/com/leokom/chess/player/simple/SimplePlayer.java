package com.leokom.chess.player.simple;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Run just 2 moves for white/black (central pawns)
 * Always agree to draw.
 * Resign on the 3'd move.
 * This player guarantees finite game.
 *
 * Author: Leonid
 * Date-time: 15.04.13 22:26
 */
public class SimplePlayer implements Player {
	private Position position = Position.getInitialPosition();
	private int moveNumber;
	private Player opponent;
	private final Logger logger = LogManager.getLogger( this.getClass() );
	private boolean recordingMode = false;

	public SimplePlayer() {
		moveNumber = 0;
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void switchToRecodingMode() {
		recordingMode = true;
	}

	@Override
	public void leaveRecordingMode() {
		recordingMode = false;
	}

	@Override
	public void joinGameForSideToMove() {
		recordingMode = false;
		executeMove( null );
	}

	@Override
	public void opponentMoved( Move opponentMove ) {
		position = position.move( opponentMove );
		if ( !recordingMode ) {
			executeMove( opponentMove );
		}
	}

	private void executeMove( Move opponentMove ) {
		LogManager.getLogger().info( "We're going to execute a move" );
		int rankFrom = position.getSideToMove() == Side.WHITE ? 2 : 7;
		int rankTo = position.getSideToMove() == Side.WHITE ? 4 : 5;
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
		position = position.move( move );
		//hiding complexity of opponent.opponentMoved call
		opponent.opponentMoved( move );
	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		LogManager.getLogger().info( "Opponent suggests me start new game white" );
		moveNumber = 0;
		position = Position.getInitialPosition();
		executeMove( null );
	}

	@Override
	public void opponentSuggestsMeStartNewGameBlack() {
		LogManager.getLogger().info( "Opponent suggests me start new game black" );
		moveNumber = 0;
		position = Position.getInitialPosition();
	}
}
