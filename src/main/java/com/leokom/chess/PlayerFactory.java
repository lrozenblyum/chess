package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayerSupplier;
import com.leokom.chess.player.legal.brain.simple.SimplePlayerSupplier;
import com.leokom.chess.player.winboard.WinboardPlayerSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * Create players for the chess game
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
final class PlayerFactory implements Function< Side, Player > {
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

		Optional<String> getFor( Side side ) {
			return Optional.ofNullable(
				System.getProperty(
					String.format( "%s.%s", side.name().toLowerCase(), propertyName )
				)
			);
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
	 * no limits on move amount and single-threaded model of execution.
	 *
	 * LegalPlayer supports optional depth parameter.
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	@Override
	public Player apply( Side side ) {
		return new ChessSystemProperty("engine").getFor(side).map(engineName -> {
			logger.info("Selecting an engine for Side = " + side + " by engine name = " + engineName);
			switch (engineName) {
				case "Legal":
					return getLegalPlayerSupplier( side );
				case "Simple":
					return new SimplePlayerSupplier();
				case "Winboard":
					return new WinboardPlayerSupplier();
				default:
					throw new IllegalArgumentException( "The engine is not supported: " + engineName);
			}
		}).orElseGet(() -> {
			logger.info( "Selecting a default engine for Side = " + side );
			return side == Side.WHITE ?	new WinboardPlayerSupplier() : getLegalPlayerSupplier( side );
		}).get();
	}

	private static LegalPlayerSupplier getLegalPlayerSupplier( Side side ) {
		return new ChessSystemProperty("depth").getFor(side)
				.map(Integer::valueOf)
				.map(LegalPlayerSupplier::new) //takes depth parameter
				.orElseGet(LegalPlayerSupplier::new); //without parameters, default constructor
	}
}
