package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.RulesBuilder;
import com.leokom.chess.player.Player;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;

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
		final UserMoveListener moveListener = userMoveListener.getValue();
		moveListener.execute( move );
	}

	private void executeDrawOfferOrClaimFromUI( WinboardCommander commander ) {
		final ArgumentCaptor<OfferDrawListener> offerDrawListener = ArgumentCaptor.forClass( OfferDrawListener.class );
		verify( commander ).onOfferDraw( offerDrawListener.capture() );
		final OfferDrawListener moveListener = offerDrawListener.getValue();
		moveListener.execute();
	}

	@Test
	public void errorMoveIsDetected() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		player.setOpponent( mock( Player.class ) );

		executeMoveFromUI( commander, "e2e5" );

		verify( commander ).illegalMove( "e2e5" );
	}

	@Test
	public void correctMoveIsNotReportedAsError() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		player.setOpponent( mock( Player.class ) );

		executeMoveFromUI( commander, "e2e4" );

		verify( commander, never() ).illegalMove( any() );
	}

	@Test
	public void noPositionUpdateForAnotherPlayerInErrorCase() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		Player opponent = mock(Player.class);
		player.setOpponent( opponent );

		executeMoveFromUI( commander, "e2e5" );

		verify( opponent, never() ).opponentMoved( any() );
	}

	@Test
	public void offerDrawTransmittedToTheOpponent() {
		WinboardCommander commander = mock( WinboardCommander.class );
		final WinboardPlayer player = new WinboardPlayer( commander );
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		makeDrawOfferFromUI( commander );
		verify( opponent ).opponentMoved( Move.OFFER_DRAW );
	}

	private void makeDrawOfferFromUI( WinboardCommander commander ) {
		final ArgumentCaptor< OfferDrawListener > offerDrawListener = ArgumentCaptor.forClass( OfferDrawListener.class );
		verify( commander ).onOfferDraw( offerDrawListener.capture() );
		offerDrawListener.getValue().execute();
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
	public void reactionToObligatoryDrawAfterOpponentMove() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();

		final Player opponent = mock( Player.class );
		doAnswer( (invocation -> { player.opponentMoved( new Move( "g8", "f6" ) );return null; } ) ).
				when( opponent ).opponentMoved( new Move( "g1", "f3" ) );

		final Position position = Position.getInitialPosition( new RulesBuilder().movesTillDraw( 1 ).build() );
		initWinboardPlayer( player, commander, opponent, position );

		executeMoveFromUI( commander, "g1f3" );

		verify( commander, never() ).checkmate( any() );
		verify( commander ).obligatoryDrawByMovesCount( 1 );
	}

	@Test
	public void reactionToClaimDrawFromOpponent() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();

		final Player opponent = mock( Player.class );

		int movesTillClaimDraw = 1;
		final Position position = Position.getInitialPosition( new RulesBuilder().movesTillClaimDraw(movesTillClaimDraw).build() );
		initWinboardPlayer( player, commander, opponent, position );

		player.opponentMoved( new Move( "g1", "f3" ) );
		executeMoveFromUI( commander, "g8f6" );
		player.opponentMoved(Move.CLAIM_DRAW);

		verify( commander ).claimDrawByMovesCount(movesTillClaimDraw);
		//we should correctly know the state
		assertTrue( player.getPosition().isTerminal() );
	}

	@Test
	public void reactionToClaimDrawFromUI() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();

		final Player opponent = mock( Player.class );

		int movesTillClaimDraw = 1;
		final Position position = Position.getInitialPosition( new RulesBuilder().movesTillClaimDraw(movesTillClaimDraw).build() );
		initWinboardPlayer( player, commander, opponent, position );

		player.opponentMoved( new Move( "g1", "f3" ) );
		executeMoveFromUI( commander, "g8f6" );
		player.opponentMoved( new Move( "b1", "c3" ) );
		executeDrawOfferOrClaimFromUI( commander );

		//we should correctly know the state
		assertTrue( player.getPosition().isTerminal() );
	}

	@Test
	public void reactionToObligatoryDrawAfterWinboardMove() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();

		final Player opponent = mock( Player.class );

		final Position position = Position.getInitialPosition( new RulesBuilder().movesTillDraw( 1 ).build() );
		initWinboardPlayer( player, commander, opponent, position );

		player.opponentMoved( new Move( "g1", "f3" ) );

		executeMoveFromUI( commander, "g8f6" );

		verify( commander, never() ).checkmate( any() );
		verify( commander ).obligatoryDrawByMovesCount( 1 );
	}

	@Test
	public void correctReactionToDrawByAgreement() {
		WinboardCommander commander = mock( WinboardCommander.class );

		final WinboardPlayer player = new WinboardPlayer();
		final Player opponent = mock( Player.class );
		doAnswer( (invocation -> { player.opponentMoved( Move.ACCEPT_DRAW );return null; } ) ).
				when( opponent ).opponentMoved( Move.OFFER_DRAW );

		final Position position = Position.getInitialPosition( new RulesBuilder().movesTillDraw( 1 ).build() );

		initWinboardPlayer( player, commander, opponent, position );

		makeDrawOfferFromUI( commander );

		verify( commander, never() ).checkmate( any() );
		verify( commander, never() ).obligatoryDrawByMovesCount( anyInt() );
		verify( commander ).agreeToDrawOffer();
	}

	private void initWinboardPlayer( WinboardPlayer player, WinboardCommander commander, Player opponent, Position position ) {
		player.setOpponent( opponent );
		player.setPosition( position );
		player.initCommander( commander );
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
		when( quitCommunicator.receive() ).thenReturn( "quit" );

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
