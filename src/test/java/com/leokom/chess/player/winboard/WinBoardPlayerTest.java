package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.RulesBuilder;
import com.leokom.chess.player.Player;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardPlayerTest {
	private static final int PROTOCOL_VERSION = 2; //any??
	private static final int WAIT_TILL_QUIT = 5000;

	@Test
	public void newCommandResetsPosition() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		//unit test is harder to program here than the integration one...

		final ArgumentCaptor<NewListener> newListener = ArgumentCaptor.forClass( NewListener.class );
		verify( commander ).onNew( newListener.capture() );

		executeMoveFromUI( commander, "e2e4" );

		assertNull( player.getPosition().getPieceType( "e2" ) );

		//'new' command will be received -> need resetting the board
		newListener.getValue().execute();
		assertNotNull( player.getPosition().getPieceType( "e2" ) );
	}

	private void executeMoveFromUI( WinboardCommander commander, String move ) {
		final ArgumentCaptor<UserMoveListener> userMoveListener = ArgumentCaptor.forClass( UserMoveListener.class );
		verify( commander ).onUserMove( userMoveListener.capture() );
		userMoveListener.getValue().execute( move );
	}

	@Test
	public void offerDrawTransmittedToTheOpponent() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		final ArgumentCaptor< OfferDrawListener > offerDrawListener = ArgumentCaptor.forClass( OfferDrawListener.class );
		verify( commander ).onOfferDraw( offerDrawListener.capture() );
		offerDrawListener.getValue().execute();
		verify( opponent ).opponentMoved( Move.OFFER_DRAW );
	}

	//this test should emulate WinBoard behaviour and analyze our reaction on it.
	//in theory in future we could extract some Winboard emulator

	@Test
	public void creationSwitchesToInitMode() {
		WinboardCommander commander = mock( WinboardCommander.class );

		//implicit call of startInit
		new WinboardPlayer( commander );

		//it really checks only 1 method call
		verify( commander ).startInit();
	}

	@Test
	public void playerCorrectReactionForDraw() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();
		final Player opponent = mock( Player.class );
		doAnswer( (invocation -> { player.opponentMoved( new Move( "g8", "f6" ) );return null; } ) ).
				when( opponent ).opponentMoved( new Move( "g1", "f3" ) );
		player.setOpponent( opponent );
		player.setPosition( Position.getInitialPosition( new RulesBuilder().movesTillDraw( 1 ).build() ) );
		player.initCommander( commander );

		executeMoveFromUI( commander, "g1f3" );

		verify( commander, never() ).checkmate( any() );
		verify( commander ).obligatoryDrawByMovesCount( 1 );
	}

	@Test
	public void startInitAsEarlyAsPossible() {
		WinboardCommander commander = mock( WinboardCommander.class );

		//implicit call of startInit
		new WinboardPlayer( commander );

		InOrder inOrder = inOrder( commander );

		inOrder.verify( commander ).startInit();
		inOrder.verify( commander ).onNew( any() );
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = WAIT_TILL_QUIT )
	public void useCommanderForQuitCommand() {
		final Communicator quitCommunicator = mock( Communicator.class );
		stub( quitCommunicator.receive() ).toReturn( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardPlayer player = new WinboardPlayer(	commander );

		player.opponentSuggestsMeStartNewGameWhite();
	}

	@Test
	public void quitCommandSwitchesShutdownNeed() {
		WinboardCommander commander = mock( WinboardCommander.class );

		WinboardPlayer player = new WinboardPlayer(	commander );

		ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		verify( commander ).onQuit( quitListener.capture() );
		assertFalse( player.needShuttingDown() );

		//correct quit listener must enable need of shutting down
		quitListener.getValue().execute();
		assertTrue( player.needShuttingDown() );
	}

	/**
	 * Correct quit listener must set up inner flag to quit
	 */
	@Test
	public void quitListenerActsCorrectly() {
		//dummy implementation - each time anybody sets a quit listener -
		//we quit IMMEDIATELY
		final WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer winboardPlayer = new WinboardPlayer( commander );

		final ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		verify( commander ).onQuit( quitListener.capture() );

		quitListener.getValue().execute();

		assertTrue( winboardPlayer.needShuttingDown() );
	}

	//ensure need of refactoring into commander instead of communicator
	//this is an integration test to ensure the loop won't be infinite
	//after receiving quit command
	@Test( timeout = WAIT_TILL_QUIT )
	public void useCommanderForQuitCommandRealTest() {
		//dummy implementation - each time anybody sets a quit listener -
		//we quit IMMEDIATELY
		final WinboardCommander commander = mock( WinboardCommander.class );

		final Player winboardPlayer = new WinboardPlayer( commander );

		final ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		verify( commander ).onQuit( quitListener.capture() );
		quitListener.getValue().execute();

		winboardPlayer.opponentSuggestsMeStartNewGameWhite();
	}

	/**
	 * validate that our WinboardPlayer
	 * in its constructor
	 * initializes the commander's protover listener
	 * in which, sets up our desired winboard properties
	 * and finishes the initialization
	 */
	@Test
	public void correctProtoverReaction() {
		//dummy implementation - each time anybody sets a protover listener -
		//we call it IMMEDIATELY
		WinboardCommander commander = mock( WinboardCommander.class );

		new WinboardPlayer(	commander );

		//the player must have set its listener in constructor...
		ArgumentCaptor< ProtoverListener > listenerCaptor = ArgumentCaptor.forClass( ProtoverListener.class );
		verify( commander ).onProtover( listenerCaptor.capture() );

		//calling the protover listener - it must have implications described below.
		listenerCaptor.getValue().execute( PROTOCOL_VERSION );

		InOrder orderedCalls = inOrder( commander );

		orderedCalls.verify( commander ).enableUserMovePrefixes();
		orderedCalls.verify( commander ).finishInit();
	}
}
