package com.leokom.chess.gui.winboard;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:16
 */
public class WinBoardCommanderTest {
	private WinboardCommander commander;
	private MockCommunicator communicator;

	@Before
	public void prepare() {
		communicator = new MockCommunicator();
		commander = new WinboardCommander( communicator );
	}

	@Test
	public void initializationStarted() {
		assertEquals( 1, communicator.getSentCommands().size() );
		assertEquals( "feature done=0", communicator.getSentCommands().get( 0 ) );
	}
}
