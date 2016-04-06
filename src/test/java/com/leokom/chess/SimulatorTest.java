package com.leokom.chess;

import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
public class SimulatorTest {
	private final Player first = mock( Player.class );
	private final Player second = mock( Player.class );

	@Test
	public void runGame() {
		new Simulator( first, second ).run();

		verify( second ).opponentSuggestsMeStartNewGameBlack();
		verify( first ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void afterFinishRunAnotherGameReversed() {
		new Simulator( first, second ).run();

		verify( first ).opponentSuggestsMeStartNewGameBlack();
		verify( second ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void getStatistics() {
		SimulatorStatistics statistics = new Simulator( first, second ).run();
		assertNotNull( statistics );
	}

	@Ignore( "long, probably need to move to IT" )
	@Test
	public void legalPlayerEqualProbableDraw() {
		new Simulator( new LegalPlayer(), new LegalPlayer() ).run();
	}
}