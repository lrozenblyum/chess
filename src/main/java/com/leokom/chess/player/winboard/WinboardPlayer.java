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

	private Logger logger = LogManager.getLogger( this.getClass() );
	private final WinboardCommander commander;
	private boolean needQuit = false;
	private Player opponent;


	//just for tests
	void setPosition( Position position ) {
		this.position = position;
	}

	//TODO: symptom of need to change architecture
	//the 'state' of game should be commonly shared
	//for both players
	private Position position = Position.getInitialPosition();

	//TODO: THINK about consequences of:
	//creating several instances of the player (must be singleton)
	//calling run several times (from different threads)

	/**
	 * Create instance on Winboard player.
	 * @param winboardCommander instance of mid-level winboard-commander.
	 */
	WinboardPlayer( WinboardCommander winboardCommander ) {
		this.commander = winboardCommander;

		commander.onXBoard( () -> logger.info( "Ready to work" ) );

		//maybe Resign/Win/Draw or other termination of game
		//should lead to quit as well?
		commander.onQuit( () -> needQuit = true );

		commander.onUserMove( new WinboardUserMoveListener() );

		commander.onGo( new GoListener() {
			@Override
			public void execute() {
				opponent.opponentSuggestsMeStartNewGameWhite();
			}
		} );

		commander.onProtover( protocolVersion -> {
			commander.enableUserMovePrefixes();
			commander.finishInit();

			logger.info( "Protocol version detected = " + protocolVersion );
		} );

		commander.onOfferDraw( new OfferDrawListener() {
			@Override
			public void execute() {
				opponent.opponentOfferedDraw();
			}
		} );

		commander.onResign( new ResignListener() {
			@Override
			public void execute() {
				opponent.opponentResigned();
			}
		} );

		commander.onGameOver( ( data ) -> opponent.opponentMoved( Move.RESIGN ) );

		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		commander.startInit();
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
		while( true ) {
			commander.processInputFromServer();
			if ( needQuit ) {
				break;
			}
		}
	}

	/**
	 * Translate Player format of move to Winboard-client
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void opponentMoved( Move opponentMove ) {
		position = position.move( opponentMove );

		if ( opponentMove == Move.RESIGN ) {
			commander.resign();
		}
		else {
			String translatedMove = opponentMove.toOldStringPresentation();
			if ( opponentMove.isPromotion() ) {
				translatedMove = translatedMove.substring( 0, PROMOTION_MOVE_LENGTH - 1 ) + translatedMove.substring( PROMOTION_MOVE_LENGTH - 1 ).toLowerCase();
			}

			commander.opponentMoved( translatedMove );

			detectCheckmate();
		}
	}

	/**
	 * Inform Winboard UI if position is checkmate
	 */
	private void detectCheckmate() {
		//TODO: there are other reasons of terminal position, not only checkmate
		if ( position.isTerminal() ) {
			commander.checkmate( position.getWinningSide() );
		}
	}

	private boolean isPromotion( String move ) {
		//well it depends on fact that Player and Winboard promotion length is the same
		//so far so good
		return move.length() == PROMOTION_MOVE_LENGTH;
	}

	@Override
	public void opponentOfferedDraw() {
		commander.offerDraw();
	}

	@Override
	public void opponentResigned() {
		commander.resign();
	}

	//TODO: validate legality of this method call!
	@Override
	public void opponentAgreedToDrawOffer() {
		commander.agreeToDrawOffer();
	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	/**
	 * Check if we need break main loop
	 * @return true if main loop must be stopped
	 */
	boolean needShuttingDown() {
		return needQuit;
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
			String translatedMove = move;
			if ( isPromotion( move ) ) {
				translatedMove = translatedMove.substring( 0, PROMOTION_MOVE_LENGTH - 1 ) + translatedMove.substring( PROMOTION_MOVE_LENGTH - 1 ).toUpperCase();
			}

			String squareFrom = translatedMove.substring( 0, SQUARE_FROM_LENGTH );
			String destination = translatedMove.substring( 2 );

			final Move engineMove = new Move( squareFrom, destination );
			position = position.move( engineMove );

			detectCheckmate();

			//important to call last
			//so that we'll won't return recursively here in another move
			//the same we did in LegalPlayer : first update OUR state
			//only THEN inform the opponent!
			opponent.opponentMoved( engineMove );
		}
	}
}
