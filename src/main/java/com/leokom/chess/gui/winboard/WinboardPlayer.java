package com.leokom.chess.gui.winboard;

import com.leokom.chess.Player;
import com.leokom.chess.gui.Communicator;
import com.leokom.chess.gui.Listener;
import org.apache.log4j.Logger;

/**
 * Central entry point to Winboard processing.
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardPlayer implements Player {
	private Communicator communicator;
	private Listener listener;
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
		this.communicator = winboardCommander.getCommunicator();
		this.commander = winboardCommander;



		commander.setXboardListener( new XBoardListener() {
			@Override
			public void execute() {
				logger.info( "Ready to work" );
			}
		});

		commander.setOfferDrawListener(
			new OfferDrawListener() {
				@Override
				public void execute() {
					//TODO: see commander.agreeToDrawOffer
					communicator.send( "offer draw" );
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
				listener.onCommandReceived();
			}
		});

		commander.setGoListener(new GoListener() {
			@Override
			public void execute() {
				listener.onCommandReceived();
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
	public void setOnMoveListener( Listener listenerToSet ) {
		this.listener = listenerToSet;
	}

	/**
	 * Run main loop that works till winboard sends us termination signal
	 */
	@Override
	public void run() {
		while( true ) {
			commander.getInput();
			if ( needQuit ) {
				break;
			}
		}
	}

	/**
	 * Send the command provided
	 * TODO: for sure it must get Command object that is xboard-independent
	 * @param command command to be sent
	 */
	@Override
	public void send( String command ) {
		this.communicator.send( command );
	}
}
