package com.leokom.chess.player.winboard;

import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

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
	private Logger logger = Logger.getLogger( this.getClass() );
	private final WinboardCommander commander;
	private boolean needQuit = false;
	private Player opponent;

	//TODO: THINK about consequences of:
	//creating several instances of the controller (must be singleton)
	//calling run several times (from different threads)

	/**
	 * Create instance on Winboard controller.
	 * @param winboardCommander instance of mid-level winboard-commander.
	 */
	WinboardPlayer( WinboardCommander winboardCommander ) {
		this.commander = winboardCommander;

		commander.onXBoard( new XBoardListener() {
			@Override
			public void execute() {
				logger.info( "Ready to work" );
			}
		} );

		commander.onQuit( new QuitListener() {
			@Override
			public void execute() {
				needQuit = true;
			}
		} );

		commander.onUserMove( new UserMoveListener() {
			@Override
			public void execute( String move ) {
				opponent.opponentMoved( move );
			}
		} );

		commander.onGo( new GoListener() {
			@Override
			public void execute() {
				//TODO: ugly null, need separate method
				opponent.opponentMoved( null );
			}
		} );

		commander.onProtover( new ProtoverListener() {
			@Override
			public void execute( int protocolVersion ) {
				commander.enableUserMovePrefixes();
				commander.finishInit();

				logger.info( "Protocol version detected = " + protocolVersion );
			}
		} );

		commander.onOfferDraw( new OfferDrawListener() {
			@Override
			public void execute() {
				opponent.opponentOfferedDraw();
			}
		} );

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
	 */
	@Override
	public void run() {
		while( true ) {
			commander.processInputFromServer();
			if ( needQuit ) {
				break;
			}
		}
	}

	//listener to another player's move
	@Override
	public void opponentMoved( String opponentMove ) {
		commander.opponentMoved( opponentMove );
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
}
