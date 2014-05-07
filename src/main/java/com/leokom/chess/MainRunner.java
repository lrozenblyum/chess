package com.leokom.chess;


import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
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

		final Player whitePlayer = PlayerFactory.createPlayer( Side.WHITE );
		final Player blackPlayer = PlayerFactory.createPlayer( Side.BLACK );
		//TODO: this double setting
		//indicates we need some master Game object
		//that will combine them together
		blackPlayer.setOpponent( whitePlayer );
		whitePlayer.setOpponent( blackPlayer );

		//TODO: it's main loop - which definitely looks out of
		//symmetry and players equality
		whitePlayer.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}

}

