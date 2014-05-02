package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 02.05.14 18:35
 */
public class LegalPlayerSelfPlayTest {
	//finite time of game finish (practically till July2014 FIDE rules in action
	//the infinite game is possible)

	// no exceptions
	@Test
	public void twoLegalPlayers() {
		Player legalPlayerWhite = new LegalPlayer( Side.WHITE );
		Player legalPlayerBlack = new LegalPlayer( Side.BLACK );

		legalPlayerWhite.setOpponent( legalPlayerBlack );
		legalPlayerBlack.setOpponent( legalPlayerWhite );

		//enforce game (strange way, yes?)!
		legalPlayerWhite.opponentMoved( null );


	}
}
