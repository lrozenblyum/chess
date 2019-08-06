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
	 * General parameters:
	 *             <ul>
	 *             <li>-Dwhite.engine=<code>engineName</code></li>
	 *             <li>-Dblack.engine=<code>engineName</code></li>
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
 	 *             <li>-Dwhite.engine=Winboard</li>
 	 *             <li>-Dblack.engine=Legal</li>
 	 *             </ul>
	 *
	 * <p>
	 *
	 * Optional parameters for LegalPlayer
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

	private static void runGame() {
		PlayerFactory factory = new PlayerFactory();
		final Player whitePlayer = factory.createPlayer( Side.WHITE );
		final Player blackPlayer = factory.createPlayer( Side.BLACK );

		new Game( whitePlayer, blackPlayer ).run();
	}

}

