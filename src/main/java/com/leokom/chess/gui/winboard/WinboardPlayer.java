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
			//TODO: any Thread.sleep needed?
			String line = communicator.receive();

			//TODO: what does it mean?
			if ( line == null ) {
				continue;
			}

			if ( line.equals( "quit" ) ) {
				logger.info( "Received quit command" );
				break;
			}

			// xboard
			// This command will be sent once immediately after your engine process is started.
			// You can use it to put your engine into "xboard mode" if that is needed.
			// If your engine prints a prompt to ask for user input,
			// you must turn off the prompt and output a newline when the "xboard" command comes in.

			//LR: because we don't print any prompt, I don't put any newline here
			if ( line.equals( "xboard" ) ) {
				logger.info( "Ready to work" );
			}


			if ( line.startsWith( "protover" ) ) {

			}

			//this is received only if we play white, is it true?
			//otherwise we'll get usermove commands instead

			//the 2'nd check works because we enabled v2 feature...
			if ( line.equals( "go" ) || line.startsWith( "usermove" ) ) {
				listener.onCommandReceived();
			}

			//another player offers draw - accept always
			if ( line.equals( "draw" ) ) {
				communicator.send( "offer draw" );
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
