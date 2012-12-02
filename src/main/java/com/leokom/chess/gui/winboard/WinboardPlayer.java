package com.leokom.chess.gui.winboard;

import com.leokom.chess.framework.Player;
import com.leokom.chess.framework.PlayerMovedListener;
import org.apache.log4j.Logger;

/**
 * Central entry point to Winboard processing.
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardPlayer implements Player {
	private PlayerMovedListener playerMovedListener;
	private Logger logger = Logger.getLogger( this.getClass() );
	private WinboardCommander commander;
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



		commander.setXboardListener( new XBoardListener() {
			@Override
			public void execute() {
				logger.info( "Ready to work" );
			}
		});

		//TODO: it's caller's responsibility! Remove this logic!
		commander.setOfferDrawListener(
			new OfferDrawListener() {
				@Override
				public void execute() {
					commander.agreeToDrawOffer();
				}
			}
		);

		commander.setQuitListener( new QuitListener() {
			@Override
			public void execute() {
				needQuit = true;
			}
		} );

		commander.setUserMoveListener(new UserMoveListener() {
			@Override
			public void execute() {
				playerMovedListener.onPlayerMoved( null );
			}
		});

		commander.setGoListener(new GoListener() {
			@Override
			public void execute() {
				playerMovedListener.onPlayerMoved( null );
			}
		});

		commander.setProtoverListener(new ProtoverListener() {
			@Override
			public void execute( int protocolVersion ) {
				commander.enableUserMovePrefixes();
				commander.finishInit();

				logger.info( "Protocol version detected = " + protocolVersion );
			}
		});

		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		commander.startInit();
	}

	//may create attach - now it's over-projecting - 1 is OK
	@Override
	public void setOnMoveListener( PlayerMovedListener playerMovedListenerToSet ) {
		this.playerMovedListener = playerMovedListenerToSet;
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
	public void onPlayerMoved( String move ) {
		commander.anotherPlayerMoved( move );
	}

	@Override
	public void onDrawOfferred() {
		commander.offerDraw();
	}

	@Override
	public void onResigned() {
		commander.resign();
	}

	/**
	 * Check if we need break main loop
	 * @return true if main loop must be stopped
	 */
	boolean needShuttingDown() {
		return needQuit;
	}
}
