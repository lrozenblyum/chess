package com.leokom.games.chess;


import com.leokom.games.chess.players.CommandLinePlayers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entry point to the Chess application.
 */
final class MainRunner {
	//prohibit instantiation
	private MainRunner() {
	}

	private static final Logger logger = LogManager.getLogger();
	private static final String BRAND_NAME = "Leokom Chess";

	/**
	 * Start whole chess program
	 * @param args currently unused.
	 *
	 * The parameters are provided via easier-to-use Java system properties way.
	 * <p>
	 * General parameters:
	 *             <ul>
	 *             <li>-Dwhite.engine=<code>engineName</code></li>
	 *             <li>-Dblack.engine=<code>engineName</code></li>
	 *             </ul>
	 *
	 * <code>engineName</code> could be any of:
	 *             <ul>
	 *             <li>ui.winboard</li>
	 *             <li>brain.simple</li>
	 *             <li>brain.denormalized</li>
	 *             <li>brain.normalized</li>
	 *             <li>brain.random</li>
	 *             </ul>
 	 *
 	 * Default players:
 	 *             <ul>
 	 *             <li>-Dwhite.engine=ui.winboard</li>
 	 *             <li>-Dblack.engine=brain.normalized</li>
 	 *             </ul>
	 *
	 * <p>
	 *
	 * Optional parameters for brain.normalized
	 * 	            <ul>
	 * 	            <li>-Dwhite.depth=<code>depth in plies</code></li>
	 * 	            <li>-Dblack.depth=<code>depth in plies</code></li>
	 * 	            </ul>
	 *
	 * <code>depth in plies</code> can be any of:
	 *             <ul>
	 *             <li>1</li>
	 *             <li>2</li>
	 *             </ul>
	 *
	 * For Winboard opponents always specify them as Black
	 *             even if they eventually start playing White.
	 * In Winboard the main decisions are taken by the WinboardPlayer itself,
	 *             so our switchers are practically just telling what's the
	 *             Winboard's opponent.
	 *
	 * Not supported player combinations:
	 * 				<ul>
	 * 					<li>Winboard vs Winboard (has no sense as 2 thin clients for UI?)</li>
	 * 					<li>Winboard vs any other engine that uses System.out has no practical use (UCI?)</li>
	 *				</ul>
	 *
	 * Combination supported with a known limitation:
	 *  brain.* vs brain.* is possible but can lead to StackOverflow due to
	 * 	no limits on move amount and single-threaded model of execution
	 * 	(although some brains like brain.simple have internal limit on count of moves).
	 *
	 * </p>
	 */
	public static void main( String[] args ) {
		try {
			logger.info( "Booting {}...", BRAND_NAME );
            new Game(
                new CommandLinePlayers()
            ).run();
            logger.info( "{} successfully completed its job. Bye-bye", BRAND_NAME );
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

}

