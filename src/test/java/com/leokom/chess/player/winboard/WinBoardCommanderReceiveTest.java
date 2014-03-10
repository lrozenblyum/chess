package com.leokom.chess.player.winboard;

import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 13.11.12 21:33
 */
public class WinBoardCommanderReceiveTest {
	@Test
	public void xboard() {
		Communicator communicator = getReceiveCommunicator( "xboard" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final XBoardListener listener = mock( XBoardListener.class );
		commander.onXBoard( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void opponentOffersDraw() {
		Communicator communicator = getReceiveCommunicator( "draw" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final OfferDrawListener listener = mock( OfferDrawListener.class );
		commander.onOfferDraw( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void opponentResigns() {
		Communicator communicator = getReceiveCommunicator( "result 1-0 {Black resigns}" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ResignListener listener = mock( ResignListener.class );
		commander.onResign( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	//TODO: I implement only simple test for usermove for 2 reasons:
	//1. I want to check if pitest finds it
	@Test
		 public void userMove() {
		Communicator communicator = getReceiveCommunicator( "usermove e2e4" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final UserMoveListener listener = mock( UserMoveListener.class );
		commander.onUserMove( listener );

		commander.processInputFromServer();
		verify( listener ).execute( "e2e4" );
	}

	@Test
	public void userMoveTriangulate() {
		Communicator communicator = getReceiveCommunicator( "usermove e7e5" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final UserMoveListener listener = mock( UserMoveListener.class );
		commander.onUserMove( listener );

		commander.processInputFromServer();
		verify( listener ).execute( "e7e5" );
	}

	@Test
	public void nonGoReceived() {
		Communicator communicator = getReceiveCommunicator( "non-go line" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );
		commander.onGo( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void goReceived() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );
		commander.onGo( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void goReceivedNoListenerConnection() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListener listener = mock( GoListener.class );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void noProtoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "Any line not starting by 'protover'" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.onProtover( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void protoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.onProtover( listener );

		commander.processInputFromServer();

		//NOTE: nice, richer checking than before (argument as well)!
		verify( listener ).execute( 2 );
	}

	@Test
	public void protoverLineWithVersionSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.onProtover( listener );

		commander.processInputFromServer();

		//NOTE: nice, richer checking than before (argument as well)!
		verify( listener ).execute( 2 );
	}

	@Test
	public void listenerNotSetNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.processInputFromServer();

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void protoverLineSentNoInputNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListener listener = mock( ProtoverListener.class );
		commander.onProtover( listener );

		verify( listener, never() ).execute( anyInt() );
	}

	@Test
	public void nonQuitLine() {
		Communicator communicator = getReceiveCommunicator( "anyNonQuitString" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListener listener = mock( QuitListener.class );
		commander.onQuit( listener );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	@Test
	public void quitLine() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListener listener = mock( QuitListener.class );
		commander.onQuit( listener );

		commander.processInputFromServer();

		verify( listener ).execute();
	}

	@Test
	public void quitWithoutListenerSet() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		//creating but not setting to commander
		final QuitListener listener = mock( QuitListener.class );

		commander.processInputFromServer();

		verify( listener, never() ).execute();
	}

	private static Communicator getReceiveCommunicator( String messageToReceive ) {
		Communicator result = mock( Communicator.class );
		stub( result.receive() ).toReturn( messageToReceive );
		return result;
	}
}