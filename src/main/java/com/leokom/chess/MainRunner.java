package com.leokom.chess;


import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point to the Chess application.
 */
public final class MainRunner {
	//prohibit instantiation
	private MainRunner() {
	}

	private static final Logger logger = LogManager.getLogger( MainRunner.class );

	/**
	 * Start whole chess program
	 * @param args currently unused.
	 *
	 * The parameters are provided via easier-to-use Java system properties way.
	 * <p>
	 * Supported parameters:
	 *             <ul>
	 *             <li>-Dwhite=<code>engineName</code></li>
	 *             <li>-Dblack=<code>engineName</code></li>
	 *             </ul>
	 *
	 * <code>engineName</code> could be any of:
	 *             <ul>
	 *             <li>Winboard</li>
	 *             <li>Simple</li>
	 *             <li>Legal</li>
	 *             </ul>
	 *
	 * Default players:
	 *             <ul>
	 *             <li>-Dwhite=Winboard</li>
	 *             <li>-Dblack=Legal</li>
	 *             </ul>
	 *
	 * For Winboard opponents always specify them as Black
	 *             even if they eventually start playing White.
	 * In Winboard the main decisions are taken by the WinboardPlayer itself,
	 *             so our switchers are practically just telling what's the
	 *             Winboard's opponent.
	 *
	 * Not supported player combinations:
	 *             <ul>
	 *                 <li>Winboard vs Winboard (has no sense as 2 thin clients for UI?)</li>
	 *             </ul>
	 * </p>
	 */
	public static void main( String[] args ) {
		try {
			logger.info( "Starting the chess..." );
			runGame();
			logger.info( "Chess are stopped. Bye-bye" );
		}
		catch ( RuntimeException re ) {
			//important to investigate issues
			//and to avoid sending console output from exception to Winboard
			logger.error( "An error occurred during the game running", re );
		}
		catch ( Error criticalError ) {
			//for example some dependent library is missing
			//trying to keep at least some information in the log
			logger.error( "A critical error occurred", criticalError );
		}

	}

	//verifying SonarCloud <-> GitHub integration
	private void emptyMethod() {

	}

	public void doSomethingBad() {
		try {
			emptyMethod();
		}
		catch ( Error error ) {

		}
	}

	private static void runGame() {
		final Player whitePlayer = PlayerFactory.createPlayer( Side.WHITE );
		final Player blackPlayer = PlayerFactory.createPlayer( Side.BLACK );

		new Game( whitePlayer, blackPlayer ).run();
	}

}

