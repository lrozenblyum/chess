package com.leokom.chess;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

	@Test
	public void statisticsReflectsReality() {
		final Position position = new PositionBuilder().winningSide( Side.BLACK ).build();

		when( first.getPosition() ).thenReturn( position );
		when( second.getPosition() ).thenReturn( position );

		SimulatorStatistics statistics = new Simulator( first, second ).run();
		assertEquals( 1, statistics.getFirstWins() );
		assertEquals( 1, statistics.getSecondWins() );
	}

	@Ignore( "long, probably need to move to IT" )
	@Test
	public void legalPlayerEqualProbableDraw() {
		new Simulator( new LegalPlayer(), new LegalPlayer() ).run();
	}
}