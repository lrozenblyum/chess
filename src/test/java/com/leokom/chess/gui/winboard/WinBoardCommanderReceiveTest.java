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
	public void noProtoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "Any line not starting by 'protover'" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.getInput();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void protoverLineSent() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.getInput();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void protoverLineWithVersionSent() {
		Communicator communicator = getReceiveCommunicator( "protover 2" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.setProtoverListener( listener );

		commander.getInput();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void listenerNotSetNoCalls() {
		Communicator communicator = getReceiveCommunicator( "protover" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final ProtoverListenerMock listener = new ProtoverListenerMock();
		commander.getInput();

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

		commander.getInput();

		assertEquals( 0, listener.callsCount );
	}

	@Test
	public void quitLine() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		final QuitListenerMock listener = new QuitListenerMock();
		commander.setQuitListener( listener );

		commander.getInput();

		assertEquals( 1, listener.callsCount );
	}

	@Test
	public void quitWithoutListenerSet() {
		Communicator communicator = getReceiveCommunicator( "quit" );
		WinboardCommander commander = new WinboardCommanderImpl( communicator );

		//creating but not setting to commander
		final QuitListenerMock listener = new QuitListenerMock();

		commander.getInput();

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
		public void execute() {
			callsCount++;
		}
	}


}
