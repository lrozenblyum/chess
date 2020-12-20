package com.leokom.games.chess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.leokom.games.chess.players.CommandLinePlayers;

/**
 * OO way to run the software
 * @author Leonid
 *
 */
class Bootstrap {
	private static final String BRAND_NAME = "Leokom Chess";
	private final Logger logger = LogManager.getLogger();

	private final Game game;

	Bootstrap() {
		this(
			new Game(
				new CommandLinePlayers()
			)
		);
	}

	Bootstrap( Game game ) {
		this.game = game;
	}

	void run() {
		try {
			logger.info( "Booting {}...", BRAND_NAME );
			GameResult gameResult = this.game.run();
			logger.info( "{} successfully completed its job. Result: {}. Bye-bye", BRAND_NAME, gameResult );
		} catch ( RuntimeException re ) {
			// important to investigate issues
			// and to avoid sending console output from exception to Winboard
			logger.error( "An error occurred during the game running", re );
		} catch ( Error criticalError ) {
			// for example some dependent library is missing
			// trying to keep at least some information in the log
			logger.error( "A critical error occurred", criticalError );
		}
	}
}
