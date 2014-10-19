package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 02.05.14 18:35
 */
@Ignore( "Till we decide what to do with potentially infinite game (stack overflow) " )
public class LegalPlayerSelfPlayTest {
	//finite time of game finish (practically till July2014 FIDE rules in action
	//the infinite game is possible)
	// no exceptions
	//too complex so far. Trying a simpler way.


	@Test
	public void twoLegalPlayers() {
		Player legalPlayerWhite = new LegalPlayer( Side.WHITE );
		Player legalPlayerBlack = new LegalPlayer( Side.BLACK );

		legalPlayerWhite.setOpponent( legalPlayerBlack );
		legalPlayerBlack.setOpponent( legalPlayerWhite );

		legalPlayerWhite.opponentSuggestsMeStartNewGameWhite();
	}

	//I've just set position but not injected it. Shouldn't have any influence !
	//but it has.
	@Test
	public void twoLegalPlayersNotInjectedPositionStrangeInfluence() {
		Player legalPlayerWhite = new LegalPlayer( Side.WHITE );
		Player legalPlayerBlack = new LegalPlayer( Side.BLACK );

		Position position = new Position();
		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.BLACK, "c1", PieceType.KING );

		legalPlayerWhite.setOpponent( legalPlayerBlack );
		legalPlayerBlack.setOpponent( legalPlayerWhite );

		legalPlayerWhite.opponentSuggestsMeStartNewGameWhite();
	}
}
