package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Before;
import org.junit.Test;

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

	//TODO: extract somewhere...
	private static Communicator getReceiveCommunicator( final String stringToReceive ) {
		return new Communicator() {
			@Override
			public void send( String command ) {
			}

			@Override
			public String receive() {
				return stringToReceive;
			}
		};
	}

	private static class ProtoverListenerMock implements ProtoverListener {
		private int callsCount = 0;

		@Override
		public void execute() {
			callsCount++;
		}
	}
}
