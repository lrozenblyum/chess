package com.leokom.chess;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import com.leokom.chess.player.simple.SimplePlayer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
public class SimulatorTest {
	private final Player first = mock( Player.class );
	private final Player second = mock( Player.class );

	private Position position = mock( Position.class );

	@Test
	public void runGame() {
		runSimulator();

		verify( second ).opponentSuggestsMeStartNewGameBlack();
		verify( first ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void afterFinishRunAnotherGameReversed() {
		runSimulator();

		verify( first ).opponentSuggestsMeStartNewGameBlack();
		verify( second ).opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void getStatistics() {
		SimulatorStatistics statistics = runSimulator();
		assertNotNull( statistics );
	}

	@Test
	public void statisticsReflectsReality() {
		this.position = new PositionBuilder().winningSide( Side.BLACK ).build();

		SimulatorStatistics statistics = runSimulator();
		assertEquals( 1, statistics.getFirstWins() );
		assertEquals( 1, statistics.getSecondWins() );
	}

	@Test
	public void twoDraws() {
		this.position = new PositionBuilder().draw().build();

		SimulatorStatistics statistics = runSimulator();
		assertEquals( 0, statistics.getFirstWins() );
		assertEquals( 0, statistics.getSecondWins() );
	}

	@Test
	public void verifyFlippingGamesCreation() {
		final Simulator simulator = new Simulator( first, second );
		programPlayers( position );
		final Simulator spy = spy( simulator );
		spy.run();

		verify( spy ).createGame( first, second );
		verify( spy ).createGame( second, first );
	}

	@Ignore( "we don't program the position well here" )
	@Test
	public void twoWinsByFirst() {
		Position whiteWins = new PositionBuilder().winningSide( Side.WHITE ).build();
		Position blackWins = new PositionBuilder().winningSide( Side.BLACK ).build();

		//thus both games are won by the First player
		programPlayers( whiteWins, blackWins );

		SimulatorStatistics statistics = getSimulator().run();
		assertEquals( 2, statistics.getFirstWins() );
		assertEquals( 0, statistics.getSecondWins() );
	}

	private Simulator getSimulator() {
		return new Simulator( first, second );
	}

	private SimulatorStatistics runSimulator() {
		//after correct game (or after full refactoring)
		//both players must return the same position : so we just stating this as a fact here
		programPlayers( this.position );

		return getSimulator().run();
	}

	private void programPlayers( Position position, Position ... positions ) {
		when( first.getPosition() ).thenReturn( position, positions );
		when( second.getPosition() ).thenReturn( position, positions );
	}

	@Ignore( "long, probably need to move to IT" )
	@Test
	public void legalPlayerEqualProbableDraw() {
		new Simulator( new LegalPlayer(), new LegalPlayer() ).run();
	}

	@Test
	public void legalVsSimpleNoCrash() {
		new Simulator( new LegalPlayer(), new SimplePlayer() ).run();
	}

	//we expect legal player is much smarter than the simple one
	@Ignore( "Offer draw is a show-stopper?" )
	@Test
	public void legalVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator(
				new SimplePlayer(), new LegalPlayer() ).run();

		assertEquals( 2, statistics.getFirstWins() );
		assertEquals( 0, statistics.getSecondWins() );
	}

	@Test
	public void simpleVsSimpleNoCrash() {
		new Simulator( new SimplePlayer(), new SimplePlayer() ).run();
	}

	@Test
	public void simpleVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator(
				new SimplePlayer(), new SimplePlayer() ).run();

		assertEquals( 1, statistics.getFirstWins() );
		assertEquals( 1, statistics.getSecondWins() );
	}
}