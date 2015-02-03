package com.leokom.chess.player.uci;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Integrating Uci Player according to the chess
 * standards
 *
 * Author: Leonid
 * Date-time: 03.02.15 22:13
 */
public class UciPlayer {


	public static Player create() {
		Logger logger = LogManager.getLogger( UciPlayer.class );

		logger.info( "Creating new UCI Player" );

		return new Player() {
			@Override
			public void opponentOfferedDraw() {

			}

			@Override
			public void opponentAgreedToDrawOffer() {

			}

			@Override
			public void opponentSuggestsMeStartNewGameWhite() {

			}

			@Override
			public void opponentMoved( Move opponentMove ) {

			}

			@Override
			public void opponentResigned() {

			}

			@Override
			public void setOpponent( Player opponent ) {

			}
		};
	}
}
