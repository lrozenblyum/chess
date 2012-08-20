package com.leokom.chess.gui;

import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Central entry point to Winboard processing.
 * Singleton to prohibit irregularities
 * Author: Leonid
 * Date-time: 20.08.12 19:28
 */
public class WinboardController {
	private Commander commander;
	private Listener listener;
	private Logger logger = Logger.getLogger( this.getClass() );

	//TODO: THINK about consequences of:
	//creating several instances of the controller (must be singleton)
	//calling run several times (from different threads)

	/**
	 * Create instance on Winboard controller.
	 * @param commander commander to be used for send-receive operations
	 */
	WinboardController( Commander commander ) {
		this.commander = commander;

		//critically important to send this sequence at the start
		//to ensure the Winboard won't ignore our 'setfeature' commands
		//set feature commands must be sent in response to protover
		commander.send( "feature done=0" );
	}

	//may create attach - now it's over-projecting - 1 is OK
	public void setOnMoveListener( Listener listenerToSet ) {
		this.listener = listenerToSet;
	}

	/**
	 * Run main loop that works till winboard sends us termination signal
	 */
	public void run() {
		while( true ) {
			//TODO: any Thread.sleep needed?
			String line = commander.receive();


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

			//protover N
			//Beginning in protocol version 2 (in which N=2), this command will be sent immediately after the "xboard" command.
			//If you receive some other command immediately after "xboard" (such as "new"), you can assume that protocol version 1 is in use.
			//The "protover" command is the only new command that xboard always sends in version 2. All other new commands to the engine are sent only if the engine first enables them with the "feature" command. Protocol versions will always be simple integers so that they can easily be compared.
			//Your engine should reply to the protover command by sending the "feature" command (see below) with the list of non-default feature settings that you require, if any.
			//Your engine should never refuse to run due to receiving a higher protocol version number than it is expecting! New protocol versions will always be compatible with older ones by default; the larger version number is simply a hint that additional "feature" command options added in later protocol versions may be accepted.
			if ( line.startsWith( "protover" ) ) {
				//TODO: add analyze if this line is received immediately after xboard
				//if not - we may assume it's protocol v1

				//enable usermove prefixes for moves for easier parsing
				commander.send( "feature usermove=1" );
				//signal end of initializations
				commander.send( "feature done=1" );

				//TODO: check if 2'nd element exists
				logger.info( "Protocol version detected = " + line.split( " " )[ 1 ] );
			}

			//this is received only if we play white, is it true?
			//otherwise we'll get usermove commands instead

			//the 2'nd check works because we enabled v2 feature...
			if ( line.equals( "go" ) || line.startsWith( "usermove" ) ) {
				listener.onCommandReceived();
			}

			//another player offers draw - accept always
			if ( line.equals( "draw" ) ) {
				commander.send( "offer draw" );
			}
		}
	}

	/**
	 * Send the command provided
	 * TODO: for sure it must get Command object that is xboard-independent
	 * @param command command to be sent
	 */
	public void send( String command ) {
		this.commander.send( command );
	}
}
