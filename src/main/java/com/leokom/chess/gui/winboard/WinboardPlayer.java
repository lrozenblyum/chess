package com.leokom.chess.gui.winboard;

import com.leokom.chess.player.DrawOfferedListener;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.NeedToGoListener;
import org.apache.log4j.Logger;

/**
 * Central entry point to Winboard processing.
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardPlayer implements Player {
	private NeedToGoListener needToGoListener;
	private Logger logger = Logger.getLogger( this.getClass() );
	private final WinboardCommander commander;
	private boolean needQuit = false;

	//TODO: THINK about consequences of:
	//creating several instances of the controller (must be singleton)
	//calling run several times (from different threads)

	/**
	 * Create instance on Winboard controller.
	 * TODO: must used commander instead of communicator...
	 * @param winboardCommander
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
				//TODO: prove this parameter passing
				needToGoListener.anotherPlayerMoved( move );
			}
		} );

		commander.onGo( new GoListener() {
			@Override
			public void execute() {
				needToGoListener.anotherPlayerMoved( null );
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

		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		commander.startInit();
	}

	//may create attach - now it's over-projecting - 1 is OK
	@Override
	public void onMoved( NeedToGoListener needToGoListenerToSet ) {
		this.needToGoListener = needToGoListenerToSet;
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

	@Override
	public void onDrawOffered( final DrawOfferedListener listener ) {
		//low-level interface adapting to the high-level
		commander.onOfferDraw( new OfferDrawListener() {
			@Override
			public void execute() {
				listener.anotherPlayerOfferedDraw();
			}
		} );
	}

	//listener to another player's move
	@Override
	public void anotherPlayerMoved( String anotherPlayerMove ) {
		commander.anotherPlayerMoved( anotherPlayerMove );
	}

	@Override
	public void anotherPlayerOfferedDraw() {
		commander.offerDraw();
	}

	@Override
	public void anotherPlayerResigned() {
		commander.resign();
	}

	//TODO: validate legality of this method call!
	@Override
	public void anotherPlayerAgreedToDrawOffer() {
		commander.agreeToDrawOffer();
	}

	/**
	 * Check if we need break main loop
	 * @return true if main loop must be stopped
	 */
	boolean needShuttingDown() {
		return needQuit;
	}
}
