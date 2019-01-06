package com.leokom.chess.player.legal;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.brain.common.Brain;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.chess.player.legal.brain.normalized.NormalizedBrain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private Player opponent;
	private Position position = Position.getInitialPosition();
	private final Brain brain;

	private boolean recordingMode;
	private Side ourSide;

	/**
	 * Create player with denormalized brain
	 */
	public LegalPlayer() {
		this( new DenormalizedBrain() );
	}

	public LegalPlayer( Brain brain ) {
		this.brain = brain;
	}

	/**
	 * Create a player with standard decision maker and injected evaluators
	 * @param brains brains to evaluate moves
	 */
	public LegalPlayer( Evaluator brains ) {
		this.brain = new NormalizedBrain( brains );
	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		getLogger().info( "Opponent suggested me starting a new game whites. Starting it" );
		position = Position.getInitialPosition();
		ourSide = Side.WHITE;
		executeOurMove();
	}

	@Override
	public void opponentSuggestsMeStartNewGameBlack() {
		getLogger().info( "Opponent suggested me starting a new game black. Starting it" );
		position = Position.getInitialPosition();
		ourSide = Side.BLACK;
	}

	@Override
	public void opponentMoved( Move... opponentMoves ) {
		LogManager.getLogger().info( "Opponent moved : {}", (Object[]) opponentMoves);
		//REFACTOR: should be part of man-in-the-middle (judge, board, validator?)
		if ( opponentMoves == null ) {
			throw new IllegalArgumentException( "Wrong opponent move null" );
		}

		//updating internal representation of our position according to the opponent's move
		Arrays.stream( opponentMoves )
		.forEach( this::updatePositionByOpponentMove );

		executeOurMove();
	}

	private void updatePositionByOpponentMove( Move opponentMove ) {
		position = position.move( opponentMove );
	}

	//exposing package-private for tests
	void executeOurMove() {
		if ( recordingMode ) {
			getLogger().info( "Just recording the moves." );
			return;
		}

		if ( position.isTerminal() ) {
			logTerminal( "the opponent's" );
			return;
		}

		//can be not our move : when opponent offers draw before HIS move
		//so he still has the right to move
		if ( position.getSideToMove() != ourSide ) {
			getLogger().info( "It's not our side to move" );

			Move bestMove = brain.findBestMoveForOpponent( position );
			if ( bestMove != null ) {
				getLogger().info( "Anyway we're ready to move: " + bestMove );
				doMoves( Collections.singletonList(bestMove) );
			}
			else {
			    getLogger().info( "We don't want to move now" );
            }
			return;
		}

		final List< Move > bestMoves = brain.findBestMove( position );
		if ( bestMoves == null ) {
		    throw new IllegalStateException( "Brain should never return null but it did that" );
        }
		if ( bestMoves.isEmpty() ) {
	        throw new IllegalStateException( "Brain doesn't want to move while the position is not terminal! It's a bug in the brain" );
		}

		doMoves( bestMoves );
	}

	private void doMoves(List<Move> bestMoves) {
		bestMoves.forEach( this::updateByOurMove );
		opponent.opponentMoved( bestMoves.toArray( new Move[]{} ) );
	}

	private void updateByOurMove( Move bestMove ) {
		if ( ! position.getMoves().contains( bestMove ) ) {
			throw new IllegalArgumentException(
					String.format( "Engine suggests to execute an illegal move: %s, legal moves: %s", bestMove, position.getMoves() )
			);
		}

		updatePositionByOurMove( bestMove );
	}

	private String getWinningSideDescription() {
		return position.getWinningSide() != null ?
				"Winner : " + position.getWinningSide() :
				"Draw" + (position.getGameResult() != null ? " REASON: " + position.getGameResult() : "");
	}

	//updating internal representation of current position
	private void updatePositionByOurMove( Move move ) {
		getLogger().info( this.position.getSideToMove() + " : Moved " + move );
		position = position.move( move );
		getLogger().info( "\nNew position : " + position );
		if ( position.isTerminal() ) {
            logTerminal( "our" );
        }
	}

    private void logTerminal(String whoseMoveItWas) {
        getLogger().info( "Final position has been reached by " + whoseMoveItWas + " move! " + getWinningSideDescription() );
    }

    private Logger getLogger() {
		return LogManager.getLogger( this.getClass() );
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void switchToRecodingMode() {
		getLogger().info( "Switching to recording mode... Moves will be executed by external source" );
		this.recordingMode = true;
	}

	@Override
	public void leaveRecordingMode() {
		getLogger().info( "Leaving recording mode" );
		this.recordingMode = false;
	}

	@Override
	public void joinGameForSideToMove() {
		getLogger().info( "Opponent suggested me to join the game for side: {}. Joining...", position.getSideToMove() );
		leaveRecordingMode();
		ourSide = position.getSideToMove();
		executeOurMove();
	}

	//injecting the position for tests, however maybe in future
	//it's useful for starting game from a non-initial position
	void setPosition( Position position, Side ourSide ) {
		this.position = position;
		this.ourSide = ourSide;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}

	public boolean isRecordingMode() {
		return recordingMode;
	}

	@Override
	public String name() {
		return Player.super.name() + " : " + this.brain.name();
	}
}
