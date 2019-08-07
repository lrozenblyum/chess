package com.leokom.chess.players;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legal.LegalPlayerSupplier;
import com.leokom.chess.player.legal.brain.denormalized.DenormalizedPlayerSupplier;
import com.leokom.chess.player.legal.brain.simple.SimplePlayerSupplier;
import com.leokom.chess.player.winboard.WinboardPlayerSupplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * Create players for the chess game based on command-line parameters
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
public final class CommandLinePlayers implements Function< Side, Player > {
	private static Logger logger = LogManager.getLogger( CommandLinePlayers.class );

	private final ChessSystemProperty engineProperty;
	private final ChessSystemProperty depthProperty;

	public CommandLinePlayers() {
		this.engineProperty = new ChessSystemProperty( "engine" );
		this.depthProperty = new ChessSystemProperty( "depth" );
	}

	/**
	 * Chess system properties.
	 * Represent properties in format 'side.property' (like 'white.depth' or 'black.engine')
	 */
	private static class ChessSystemProperty {
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
	 * BLACK: brain.normalized
	 *
	 * There are practical important limitations (not yet validated):
	 *
	 * Winboard vs Winboard game has no practical use (both will work with System.out)
	 * Winboard vs any other engine that uses System.out has no practical use (UCI?)
	 *
	 * brain.* vs brain.* is possible but can lead to StackOverflow due to
	 * no limits on move amount and single-threaded model of execution
	 * (although some brains like brain.simple have internal limit on count of moves).
	 *
	 * brain.normalized supports optional depth parameter.
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	@Override
	public Player apply( Side side ) {
		return engineProperty.getFor(side).map(engineName -> {
			logger.info("Selecting an engine for Side = " + side + " by engine name = " + engineName);
			switch (engineName) {
				case "brain.normalized":
					return getLegalPlayerSupplier( side );
				case "brain.denormalized":
					return new DenormalizedPlayerSupplier();
				case "brain.simple":
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

	private LegalPlayerSupplier getLegalPlayerSupplier( Side side ) {
		return depthProperty.getFor(side)
				.map(Integer::valueOf)
				.map(LegalPlayerSupplier::new) //takes depth parameter
				.orElseGet(LegalPlayerSupplier::new); //without parameters, default constructor
	}
}
