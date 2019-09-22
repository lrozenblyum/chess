package com.leokom.games.chess.players;

import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.Player;
import com.leokom.games.chess.player.legal.LegalPlayer;
import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.games.chess.player.legal.brain.normalized.NormalizedChessBrain;
import com.leokom.games.chess.player.legal.brain.random.RandomBrain;
import com.leokom.games.chess.player.legal.brain.simple.SimpleBrain;
import com.leokom.games.chess.player.winboard.WinboardPlayer;
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
	private static final Logger logger = LogManager.getLogger( CommandLinePlayers.class );

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
	 * Create a player for the side basing on defaults or system properties.
	 * 
	 * @param side side to create
	 * @return new instance of a player
	 */
	@Override
	public Player apply( Side side ) {
		String engineName = engineProperty.getFor( side ).orElseGet( () -> {
			logger.info( "Selecting a default engine for Side = " + side );
			return side == Side.WHITE ?	"ui.winboard" : "brain.normalized";
		} );

		return getPlayer( side, engineName );
	}

	private Player getPlayer( Side side, String engineName ) {
		logger.info("Selecting an engine for Side = {} by engine name = {}", side, engineName);
		switch (engineName) {
			case "brain.normalized":
				int depth = depthProperty.getFor(side)
						.map(Integer::valueOf)
						.orElse( 1 ); //this depth has been used for years
				return new LegalPlayer( new NormalizedChessBrain( new MasterEvaluator(), depth ) );
			case "brain.denormalized":
				return new LegalPlayer( new DenormalizedBrain() );
			case "brain.simple":
				return new LegalPlayer( new SimpleBrain() );
			case "brain.random":
				return new LegalPlayer( new RandomBrain() );
			case "ui.winboard":
				return new WinboardPlayer();
			default:
				throw new IllegalArgumentException( "The engine is not supported: " + engineName);
		}
	}
}
