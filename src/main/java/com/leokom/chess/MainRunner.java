package com.leokom.chess;


import com.leokom.chess.player.Player;
import com.leokom.chess.player.winboard.WinboardFactory;
import com.leokom.chess.player.legalMover.EnginePlayer;
import org.apache.log4j.Logger;

/**
 * Entry point to the Chess application.
 */
public final class MainRunner {
	//prohibit instantiation
	private MainRunner() {
	}

	private static final Logger logger = Logger.getLogger( MainRunner.class );

	public static void main( String[] args ) {
		logger.info( "Starting the chess..." );

		//the player must be just a thin client over Winboard
		final Player winboardPlayer = WinboardFactory.getPlayer();

		//this is the real 'brains'
		final EnginePlayer enginePlayer = new EnginePlayer();
		enginePlayer.setOpponent( winboardPlayer );
		winboardPlayer.setOpponent( enginePlayer );
		//it's main loop - which definitely looks out of
		//symmetry and players equality
		winboardPlayer.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}

}

