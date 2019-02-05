package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayerSupplier;
import com.leokom.chess.player.legal.brain.simple.SimplePlayerSupplier;
import com.leokom.chess.player.winboard.WinboardPlayerSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Create players for the chess game
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
final class PlayerFactory {
	private PlayerFactory() {}

	private static Logger logger = LogManager.getLogger( PlayerFactory.class );

	/**
	 * Chess system properties.
	 * Represent properties in format 'side.property' (like 'white.depth' or 'black.engine')
	 */
	static class ChessSystemProperty {
		private final String propertyName;

		ChessSystemProperty( String propertyName ) {
			this.propertyName = propertyName;
		}

		String getFor( Side side ) {
			return System.getProperty( side.name().toLowerCase() + "." + propertyName );
		}
	}

	/**
	 * Create player for the side
	 * Basing on defaults or system properties.
	 * Defaults :
	 * WHITE: Winboard
	 * BLACK: Legal
	 *
	 * There are practical important limitations (not yet validated):
	 *
	 * Winboard vs Winboard game has no practical use (both will work with System.out)
	 * Winboard vs any other engine that uses System.out has no practical use (UCI?)
	 *
	 * LegalPlayer vs LegalPlayer is possible but can lead to StackOverflow due to
	 * no limits on move amount and single-threaded model of execution
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	static Player createPlayer( Side side ) {
		final String engineName = new ChessSystemProperty( "engine" ).getFor( side );

		logger.info( "Engine from system properties: " + engineName + ". Side = " + side );

		return selectPlayer( side, engineName ).get();
	}

	private static Supplier< Player > selectPlayer( Side side, String engineName ) {
		if ( engineName == null ) {
			logger.info( "No selection done. Selecting default player" );
			return getDefaultPlayer( side );
		}

		switch ( engineName ) {
			case "Legal":
				String depthProperty = new ChessSystemProperty( "depth" ).getFor( side );
				return depthProperty != null ?
						new LegalPlayerSupplier( Integer.valueOf( depthProperty ) ) :
						new LegalPlayerSupplier();

			case "Simple":
				return new SimplePlayerSupplier();
			case "Winboard":
				return new WinboardPlayerSupplier();
			default:
				logger.warn( "Unsupported option specified. Selecting default player" );
				return getDefaultPlayer( side );
		}
	}

	private static Supplier< Player > getDefaultPlayer( Side side ) {
		logger.info( "Selecting default engine for Side = " + side );
		return side == Side.WHITE ?	new WinboardPlayerSupplier() : new LegalPlayerSupplier();
	}
}
