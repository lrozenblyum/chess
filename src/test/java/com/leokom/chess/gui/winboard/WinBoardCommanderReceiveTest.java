package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Test;

import static com.leokom.chess.gui.winboard.MockCommunicatorReceiveCreator.getReceiveCommunicator;
import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 13.11.12 21:33
 */
public class WinBoardCommanderReceiveTest {
	@Test
	public void xboard() {
		Communicator communicator = getReceiveCommunicator( "xboard" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final XBoardListenerMock listener = new XBoardListenerMock();
		commander.setXboardListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void opponentOffersDraw() {
		Communicator communicator = getReceiveCommunicator( "draw" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final OfferDrawListenerMock listener = new OfferDrawListenerMock();
		commander.setOfferDrawListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	//TODO: I implement only simple test for usermove for 2 reasons:
	//1. I want to check if pitest finds it
	//2. I need to check deeper what's the correct format
	@Test
	public void userMove() {
		Communicator communicator = getReceiveCommunicator( "usermove e2e4" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final UserMoveListenerMock listener = new UserMoveListenerMock();
		commander.setUserMoveListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void nonGoReceived() {
		Communicator communicator = getReceiveCommunicator( "non-go line" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListenerMock listener = new GoListenerMock();
		commander.setGoListener( listener );

		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void goReceived() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListenerMock listener = new GoListenerMock();
		commander.setGoListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void goReceivedNoListenerConnection() {
		Communicator communicator = getReceiveCommunicator( "go" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final GoListenerMock listener = new GoListenerMock();

		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void noProtoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "Any line not starting by 'protover'" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void protoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void protoverLineWithVersionSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void listenerNotSetNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void protoverLineSentNoInputNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		assertEquals( 0, listener.callsCount );
	}



	@Test
	public void nonQuitLine() {
		Communicator communicator = getReceiveCommunicator( "anyNonQuitString" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListenerMock listener = new QuitListenerMock();
		commander.setQuitListener( listener );

		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void quitLine() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListenerMock listener = new QuitListenerMock();
		commander.setQuitListener( listener );

		commander.processInputFromServer();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void quitWithoutListenerSet() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		//creating but not setting to commander
		final QuitListenerMock listener = new QuitListenerMock();

		commander.processInputFromServer();

		assertEquals( 0, listener.callsCount );
	}

	private static class QuitListenerMock implements QuitListener {
		private int callsCount = 0;
		@Override
		public void execute() {
			callsCount++;
		}
	}

	private static class ProtoverListenerMock implements ProtoverListener {
		private int callsCount = 0;

		@Override
		public void execute( int protocolVersion ) {
			callsCount++;
		}
	}

	private static class GoListenerMock implements GoListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}

	private static class UserMoveListenerMock implements UserMoveListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}

	private static class OfferDrawListenerMock implements OfferDrawListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}

	private static class XBoardListenerMock implements XBoardListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}
}
