package com.leokom.chess;


import com.leokom.chess.player.Player;
import com.leokom.chess.player.simple.SimpleEnginePlayer;
import com.leokom.chess.player.winboard.WinboardPlayer;
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

		final Player winboardPlayer = WinboardPlayer.create();
		final Player enginePlayer = new SimpleEnginePlayer();
		//TODO: this double setting
		//indicates we need some master Game object
		//that will combine them together
		enginePlayer.setOpponent( winboardPlayer );
		winboardPlayer.setOpponent( enginePlayer );

		//TODO: it's main loop - which definitely looks out of
		//symmetry and players equality
		winboardPlayer.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}

}

