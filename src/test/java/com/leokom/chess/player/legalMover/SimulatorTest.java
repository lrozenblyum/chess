package com.leokom.chess.player.legalMover;

import com.leokom.chess.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
public class SimulatorTest {
	@Test
	public void runGame() {
		final Player first = mock( Player.class );
		final Player second = mock( Player.class );
		new Simulator( first, second ).run();

		verify( second ).opponentSuggestsMeStartNewGameBlack();
		verify( first ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void afterFinishRunAnotherGameReversed() {
		final Player first = mock( Player.class );
		final Player second = mock( Player.class );
		new Simulator( first, second ).run();

		verify( first ).opponentSuggestsMeStartNewGameBlack();
		verify( second ).opponentSuggestsMeStartNewGameWhite();
	}
}