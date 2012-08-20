package com.leokom.chess;


import com.leokom.chess.gui.Listener;
import com.leokom.chess.gui.Controller;
import com.leokom.chess.gui.winboard.WinboardFactory;
import org.apache.log4j.Logger;

/**
 * Entry point to the Chess application.
 */
public class MainRunner {
	//prohibit instantiation
	private MainRunner() {
	}

	private static final Logger logger = Logger.getLogger( MainRunner.class );

	public static void main( String[] args ) {
		logger.info( "Starting the chess..." );

		final Controller controller = WinboardFactory.getController();

		final Listener onMoveListener = new Listener() {
			//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
			int moveNumber = 0;

			@Override
			public void onCommandReceived() {
				moveNumber++;
				logger.info( "Detected allowance to go. Move number = " + moveNumber );
				switch ( moveNumber ) {
					case 1:
						controller.send( "move e2e4" );
						break;
					case 2:
						controller.send( "move d2d4" );
						//NOTE: interesting to implement - how much do we need to wait for result?
						//NOTE2: it's not recommended way to offer draw after the move.
						controller.send( "offer draw" );
						break;
					default:
						controller.send( "resign" );
				}
			}
		};

		controller.setOnMoveListener( onMoveListener );
		//it's main loop
		controller.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}
}

