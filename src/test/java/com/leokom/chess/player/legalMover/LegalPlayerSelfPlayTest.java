package com.leokom.chess.player.legalMover;

import com.leokom.chess.Game;
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
		new Game( getLegalPlayer(), getLegalPlayer() ).run();
	}

	private LegalPlayer getLegalPlayer() {
		return new LegalPlayer( new MasterEvaluatorTweaked() );
	}

	//Second test is hanging here. Shouldn't have any influence !
	//but it has.
	// add for experiment @FixMethodOrder( MethodSorters.NAME_ASCENDING )
	// to change order of execution
	@Test
	public void secondTestInfluence() {
		new Game( getLegalPlayer(), getLegalPlayer() ).run();
	}
}
