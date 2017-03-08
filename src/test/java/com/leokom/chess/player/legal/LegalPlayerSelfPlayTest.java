package com.leokom.chess.player.legal;

import com.leokom.chess.Game;
import com.leokom.chess.player.legal.brain.normalized.MasterEvaluatorTweaked;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Author: Leonid
 * Date-time: 02.05.14 18:35
 */
@Ignore( "Performance is still not ideal. It was 2 minutes 02 sec, now 1 min 10 sec, or even 48 seconds." +
		"If the logging in MasterEvaluator is disabled : 55 sec. Enabled : ~3 min 15 sec." )
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

	//Second test is hanging here when Rules = BEFORE_JULY_2014.
	// Shouldn't have any influence ! but it has.
	// add for experiment @FixMethodOrder( MethodSorters.NAME_ASCENDING )
	// to change order of execution
	@Test
	public void secondTestInfluence() {
		new Game( getLegalPlayer(), getLegalPlayer() ).run();
	}
}
