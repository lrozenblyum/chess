package com.leokom.chess.gui.winboard;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:16
 */
public class WinBoardCommanderSendTest {
	private WinboardCommander commander;
	private MockCommunicatorSend communicatorSend;

	@Before
	public void prepare() {
		communicatorSend = new MockCommunicatorSend();
		commander = new WinboardCommanderImpl( communicatorSend );
	}

	@Test
	public void noCommandsSendFromScratch() {
		assertEquals( 0, communicatorSend.getSentCommands().size() );
	}

	@Test
	public void initializationStarted() {
		commander.startInit();
		assertEquals( 1, communicatorSend.getSentCommands().size() );
		assertEquals( "feature done=0", communicatorSend.getSentCommands().get( 0 ) );
	}

	@Test
	public void userMovesPrefixes() {
		commander.enableUserMovePrefixes();
		assertEquals( 1, communicatorSend.getSentCommands().size() );
		assertEquals( "feature usermove=1", communicatorSend.getSentCommands().get( 0 ) );
	}

	@Test
	public void initializationFinished() {
		commander.finishInit();
		assertEquals( 1, communicatorSend.getSentCommands().size() );
		assertEquals( "feature done=1", communicatorSend.getSentCommands().get( 0 ) );
	}

	@Test
	public void agreeToDraw() {
		commander.agreeToDrawOffer();

		assertEquals( 1, communicatorSend.getSentCommands().size() );
		//weird but fact... This command is used also to agree to draw.
		assertEquals( "offer draw", communicatorSend.getSentCommands().get( 0 ) );
	}

	@Test
	public void anotherPlayerMove() {
		commander.anotherPlayerMoved( "e2e4" );

		assertEquals( 1, communicatorSend.getSentCommands().size() );
		//weird but fact... This command is used also to agree to draw.
		assertEquals( "move e2e4", communicatorSend.getSentCommands().get( 0 ) );
	}
}
