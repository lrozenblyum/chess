package com.leokom.games.chess;

import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.PositionBuilder;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.Player;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

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
        assertEquals( new SimulatorStatistics( 2, 1, 1 ), statistics );
    }

    @Test
    public void twoDraws() {
        this.position = new PositionBuilder().draw( Side.WHITE ).build();

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

    private SimulatorStatistics runSimulator() {
        //after correct game (or after full refactoring)
        //both players must return the same position : so we just stating this as a fact here
        programPlayers( this.position );

        return getSimulator().run();
    }

    private Simulator getSimulator() {
        return new Simulator( first, second );
    }

    private void programPlayers( Position position, Position ... positions ) {
        when( first.getPosition() ).thenReturn( position, positions );
        when( second.getPosition() ).thenReturn( position, positions );
    }
}
