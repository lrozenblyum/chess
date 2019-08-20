package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 'Thin' player.
 * Implementation of the Player interface
 * that is used to fit generic chess processing purposes.
 * It adapts the huge Winboard interface to the Player interface.
 * The main processing is done on the Winboard-side
 * (on which a real human or some engine could be playing)
 *
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardPlayer implements Player {
	//like e7e8q
	private static final int PROMOTION_MOVE_LENGTH = 5;
	//like e7
	private static final int SQUARE_FROM_LENGTH = 2;

	private final Logger logger = LogManager.getLogger( this.getClass() );
	private WinboardCommander commander;
	private boolean needQuit = false;
	private Player opponent;

	//just for tests
	void setPosition( Position position ) {
		this.position = position;
	}

	//the To Do already exists in Game class
	//symptom of need to change architecture
	//the 'state' of game should be commonly shared
	//for both players
	private Position position = Position.getInitialPosition();

	//TODO: THINK about consequences of:
	//creating several instances of the player (must be singleton)
	//calling run several times (from different threads)

	/* For delayed initialization of spy in test.
	 * Complex scenario to ensure commander will be associated with the spy
	 * not with the original player */
	WinboardPlayer() {
	}

	/**
	 * Create instance on Winboard player.
	 *
	 * @param winboardCommander instance of mid-level winboard-commander.
	 */
	WinboardPlayer( WinboardCommander winboardCommander ) {
		initCommander( winboardCommander );
	}

	void initCommander( WinboardCommander winboardCommander ) {
		this.commander = winboardCommander;
		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		// From spec: If needed, it is okay for your engine to set done=0 soon as it starts, even before it receives the xboard and protover commands. This can be useful if your engine takes a long time to initialize itself.
		commander.startInit();

		commander.onXBoard( () -> logger.info( "Ready to work" ) );

		//maybe Resign/Win/Draw or other termination of game
		//should lead to quit as well?
		commander.onQuit( () -> needQuit = true );

		commander.onUserMove( new WinboardUserMoveListener() );

		commander.onForce( () -> opponent.switchToRecodingMode() );
		commander.onGo( () -> opponent.joinGameForSideToMove() );
		commander.onNew( () -> {
			position = Position.getInitialPosition();
			opponent.leaveRecordingMode();
			opponent.opponentSuggestsMeStartNewGameBlack();
		} );

		commander.onProtover( protocolVersion -> {
			commander.enableUserMovePrefixes();
			commander.finishInit();

			logger.info( "Protocol version detected = " + protocolVersion );
		} );

		//there is no 'onAcceptDraw' in Winboard protocol
		//using onGameOver to detect that state

		// 'draw' command may indicate both draw offer and draw claim, here we work on distinguish these states
		commander.onOfferDraw( () -> {
			Move drawMoveReceived = classifyDrawOfferCommand();

			//common approach: update internal state, detect whether we need to inform the UI about sth special
			// (like we do in detectGameOver)
			//only then - inform the opponent
			position = position.move( drawMoveReceived );

			if ( drawMoveReceived == Move.CLAIM_DRAW ) {
				commander.informAboutClaimDrawFromUIByMovesCount( position.getRules().getMovesTillClaimDraw() );
			}

			opponent.opponentMoved( drawMoveReceived );
		});

		commander.onGameOver( gameOverDetails -> {
			logger.info( "Game over. Extra details: " + gameOverDetails );
			if ( position.isTerminal() ) {
				logger.info( "We already knew about the game over due to terminal position" );
				//e.g. this can occur due to 75 moves draw.
			} else {
				//TODO: game over is sent due to draw, checkmate, resign,...
				// it's hard but need to avoid false detection

				final Move move = isDrawResult( gameOverDetails ) ? classifyDrawResult() : Move.RESIGN;

				position = position.move( move );

				opponent.opponentMoved( move );
			}
		} );
	}

	private boolean isDrawResult( String gameOverDetails ) {
		return gameOverDetails.startsWith( "1/2-1/2" );
	}

	//as a matter of fact Winboard sends us 'draw' in case when adjudication is disabled
	//during claim draw
	//when we have a legal claim draw and accept draw (possible but unlikely)
	//then we have NO choice to solve the ambiguity
	private Move classifyDrawOfferCommand() {
		return canClaimDrawBeExecutedNow() ? Move.CLAIM_DRAW : Move.OFFER_DRAW;
	}

	private Move classifyDrawResult() {
		//very loose check. Draw by insufficient material
		//can be treated here as ACCEPT_DRAW

		//when we have a legal claim draw and accept draw (possible but unlikely)
		//then we have NO choice to solve the ambiguity
		return canClaimDrawBeExecutedNow() ? Move.CLAIM_DRAW : Move.ACCEPT_DRAW;
	}

	private boolean canClaimDrawBeExecutedNow() {
		return position.getMoves().contains( Move.CLAIM_DRAW );
	}

	/**
	 * Create an instance of generic Player,
	 * who is able to play against existing WinBoard-powered player
	 * This Winboard-powered opponent could be a human
	 * running the WinBoard-powered software or another engine
	 * that can communicate via Winboard protocol
	 *
	 * @return instance of properly initialized Player against WinBoard-powered player
	 *
	 */
	public static Player create() {
		//TODO: implement some singleton policy?
		final WinboardCommunicator communicator = new WinboardCommunicator();
		return new WinboardPlayer( new WinboardCommanderImpl( communicator ) );
	}

	/**
	 * Run main loop that works till winboard sends us termination signal
	 *
	 * Clearly indicates we support only white so far.
	 */
	@Override
	public void opponentSuggestsMeStartNewGameWhite() {
		do {
			commander.processInputFromServer();
		} while ( !needQuit );
	}

	@Override
	public void opponentSuggestsMeStartNewGameBlack() {
		//TODO: anything to implement?
	}

	/**
	 * Translate Player format of move to Winboard-client
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void opponentMoved( Move... opponentMoves ) {
		for ( Move opponentMove : opponentMoves ) {
			position = position.move( opponentMove );

			if ( opponentMove == Move.RESIGN ) {
				commander.resign();
			}
			else if ( opponentMove == Move.OFFER_DRAW ) {
				commander.offerDraw();
			}
			else if ( opponentMove == Move.ACCEPT_DRAW ) {
				commander.agreeToDrawOffer();
			}
			else if ( opponentMove == Move.CLAIM_DRAW ) {
				commander.claimDrawByMovesCount( position.getRules().getMovesTillClaimDraw() );
			}
			else {
				String translatedMove = opponentMove.toOldStringPresentation();
				if ( opponentMove.isPromotion() ) {
					translatedMove = translatedMove.substring( 0, PROMOTION_MOVE_LENGTH - 1 ) + translatedMove.substring( PROMOTION_MOVE_LENGTH - 1 ).toLowerCase();
				}

				commander.opponentMoved( translatedMove );

				detectGameOver();
			}
		}
	}

	/**
	 * Inform Winboard UI when the game is over
	 */
	private void detectGameOver() {
		if ( !position.isTerminal() ) {
			return;
		}

		//technically theoretically here we can have no checkmate but:
		// - win by time
		// - ?
		if ( position.getWinningSide() != null ) {
			//TODO: there are other reasons of terminal position, not only checkmate

			commander.checkmate( position.getWinningSide() );
		}
		else {
			switch ( position.getGameResult() ) {
				case STALEMATE:
					commander.stalemateDraw();
					break;
				case DRAW_BY_OBLIGATORY_MOVES:
					commander.obligatoryDrawByMovesCount( position.getRules().getMovesTillDraw().orElse( 0 ) );
					break;
				default:
					throw new IllegalArgumentException( "Unknown game result : " + position.getGameResult() );
			}
		}
	}

	private boolean isPromotion( String move ) {
		//well it depends on fact that Player and Winboard promotion length is the same
		//so far so good
		return move.length() == PROMOTION_MOVE_LENGTH;
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	@Override
	public void switchToRecodingMode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void leaveRecordingMode() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void joinGameForSideToMove() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Check if we need break main loop
	 * @return true if main loop must be stopped
	 */
	boolean needShuttingDown() {
		return needQuit;
	}

	@Override
	public Position getPosition() {
		return position;
	}

	private class WinboardUserMoveListener implements UserMoveListener {

		/**
		 * Convert Winboard move to Chess move
		 * Winboard format :
		 * Normal moves: e2e4
		 * Pawn promotion: e7e8q
		 * Castling: e1g1, e1c1, e8g8, e8c8
		 *
		 * @param move move received from Winboard
		 */
		@Override
		public void execute( String move ) {
			final Move engineMove = translateMove( move );

			if ( !position.isLegal( engineMove ) ) {
				//the original move should be passed back to the UI
				commander.illegalMove( move );
				return;
			}

			position = position.move( engineMove );

			detectGameOver();

			//important to call last
			//so that we'll won't return recursively here in another move
			//the same we did in LegalPlayer : first update OUR state
			//only THEN inform the opponent!
			opponent.opponentMoved( engineMove );
		}

		/**
		 * Translate move from Winboard to LeokomChess engine move
		 * @param move winboard move
		 * @return engine move
		 */
		private Move translateMove( String move ) {
			String translatedMove = move;
			if ( isPromotion( move ) ) {
				translatedMove = translatedMove.substring( 0, PROMOTION_MOVE_LENGTH - 1 ) + translatedMove.substring( PROMOTION_MOVE_LENGTH - 1 ).toUpperCase();
			}

			String squareFrom = translatedMove.substring( 0, SQUARE_FROM_LENGTH );
			String destination = translatedMove.substring( 2 );

			return new Move( squareFrom, destination );
		}
	}
}
