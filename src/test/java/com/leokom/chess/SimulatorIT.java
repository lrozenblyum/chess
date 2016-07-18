package com.leokom.chess;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.*;
import com.leokom.chess.player.simple.SimplePlayer;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 06.04.16 21:08
 */
public class SimulatorIT {
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
		assertEquals( new SimulatorStatistics( 2, 1, 1 ), statistics );
	}

	@Test
	public void twoDraws() {
		this.position = new PositionBuilder().draw().build();

		SimulatorStatistics statistics = runSimulator();
		assertEquals( new SimulatorStatistics( 2, 0, 0 ), statistics );
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

	@Test
	public void verifyMultiTimesFlippingGamesCreation() {
		final Simulator simulator = new Simulator( first, second ).gamePairs( 2 );
		programPlayers( position );
		final Simulator spy = spy( simulator );
		spy.run();

		verify( spy, times( 2 ) ).createGame( first, second );
		verify( spy, times( 2 ) ).createGame( second, first );
	}


	@Test
	public void totalGamesCounted() {
		programPlayers( position );
		final SimulatorStatistics simulator = new Simulator( first, second ).gamePairs( 23 ).run();
		//twice
		assertEquals( 46, simulator.getTotalGames() );
	}

	@Ignore( "we don't program the position well here" )
	@Test
	public void twoWinsByFirst() {
		Position whiteWins = new PositionBuilder().winningSide( Side.WHITE ).build();
		Position blackWins = new PositionBuilder().winningSide( Side.BLACK ).build();

		//thus both games are won by the First player
		programPlayers( whiteWins, blackWins );

		SimulatorStatistics statistics = getSimulator().run();
		assertEquals( new SimulatorStatistics( 2, 2, 0 ), statistics );
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

	@Test
	public void legalVsSimpleNoCrash() {
		new Simulator( new LegalPlayer(), new SimplePlayer() ).run();
	}

	//we expect legal player is much smarter than the simple one
	@Test
	public void legalVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator(
				new LegalPlayer(), new SimplePlayer() ).run();

		assertEquals( new SimulatorStatistics( 2, 2, 0 ), statistics );
	}

	@Test
	public void simpleVsSimpleNoCrash() {
		new Simulator( new SimplePlayer(), new SimplePlayer() ).run();
	}

	@Test
	public void simpleVsSimpleStatistics() {
		final SimulatorStatistics statistics = new Simulator(
				new SimplePlayer(), new SimplePlayer() ).run();

		assertEquals( new SimulatorStatistics( 2, 1, 1 ), statistics );
	}

	//non-deterministic, it's not a business-requirement
	@Test
	public void legalVsLegalCustomEvaluator() {
		final Evaluator brainLikesToEatPieces = new MasterEvaluatorBuilder().weight( EvaluatorType.MATERIAL, 100.0 ).build();

		final SimulatorStatistics statistics =
			new Simulator( new LegalPlayer(), new LegalPlayer( brainLikesToEatPieces ) ).run();

		//who eats - that one wins
		assertEquals( new SimulatorStatistics( 2, 0, 2 ), statistics );
	}

	//non-deterministic, it's not a business-requirement
	@Test
	public void legalPlayerEqualProbableDraw() {
		final SimulatorStatistics statistics = new Simulator( new LegalPlayer(), new LegalPlayer() ).run();

		assertEquals( new SimulatorStatistics( 2, 1, 1 ), statistics );
	}
}