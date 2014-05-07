package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import com.leokom.chess.player.simple.SimpleEnginePlayer;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.apache.log4j.Logger;

/**
 * Create players for the chess game
 *
 * Author: Leonid
 * Date-time: 06.05.14 22:45
 */
class PlayerFactory {
	private static Logger logger = Logger.getLogger( PlayerFactory.class );

	/**
	 * Create player for the side
	 * Basing on defaults or system properties.
	 * Defaults :
	 * WHITE: Winboard
	 * BLACK: SimpleEngine
	 *
	 * @param side side to create
	 * @return new instance of a player
	 */
	static Player createPlayer( Side side ) {
		if ( side == Side.WHITE ) {
			final String engineName = System.getProperty( "white" );
			if ( "LegalPlayer".equals( engineName ) ) {
				return new LegalPlayer( side );
			}

			return WinboardPlayer.create();
		}

		final String engineName = System.getProperty( "black" );
		logger.info( "Engine from system properties: " + engineName );
		if ( "LegalPlayer".equals( engineName ) ) {
			return new LegalPlayer( side );
		}

		logger.info( "Selecting default engine" );
		return new SimpleEnginePlayer( side );
	}
}
