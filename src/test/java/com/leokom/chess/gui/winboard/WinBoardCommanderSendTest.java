package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:16
 */
public class WinBoardCommanderSendTest {
	private WinboardCommander commander;
	private MockCommunicator communicator;

	@Before
	public void prepare() {
		communicator = new MockCommunicator();
		commander = new WinboardCommanderImpl( communicator );
	}

	@Test
	public void noCommandsSendFromScratch() {
		assertEquals( 0, communicator.getSentCommands().size() );
	}

	@Test
	public void initializationStarted() {
		commander.startInit();
		assertEquals( 1, communicator.getSentCommands().size() );
		assertEquals( "feature done=0", communicator.getSentCommands().get( 0 ) );
	}

	@Test
	public void userMovesPrefixes() {
		commander.enableUserMovePrefixes();
		assertEquals( 1, communicator.getSentCommands().size() );
		assertEquals( "feature usermove=1", communicator.getSentCommands().get( 0 ) );
	}

	@Test
	public void initializationFinished() {
		commander.finishInit();
		assertEquals( 1, communicator.getSentCommands().size() );
		assertEquals( "feature done=1", communicator.getSentCommands().get( 0 ) );
	}

	@Test
	public void agreeToDraw() {
		commander.agreeToDrawOffer();

		assertEquals( 1, communicator.getSentCommands().size() );
		//weird but fact... This command is used also to agree to draw.
		assertEquals( "offer draw", communicator.getSentCommands().get( 0 ) );
	}
}
