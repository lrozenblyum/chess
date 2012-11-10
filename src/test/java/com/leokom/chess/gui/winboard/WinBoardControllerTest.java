package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Controller;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardControllerTest {

	//this test should emulate WinBoard behaviour and analyze our reaction on it.
	//in theory in future we could extract some Winboard emulator
	@Test
	public void switchesWinboardToSetUpMode() {
		//The commander mock is actually EMULATOR OF Winboard behaviour!
		//TODO: think about Mockito usage?
		MockCommunicator communicator = new MockCommunicator();

		final Controller controller = new WinboardController( communicator, new WinboardCommanderImpl( communicator ) );

		assertEquals( 1, communicator.getSentCommands().size() );
		final String initializationString = "feature done=0";
		assertEquals( initializationString, communicator.getSentCommands().get( 0 ) );
	}

	@Test
	public void creationSwitchesToInitMode() {
		//TODO: this will be not needed when controller doesn't depend on it...
		MockCommunicator communicator = new MockCommunicator();

		MockCommander commander = new MockCommander();

		WinboardController controller = new WinboardController( communicator, commander );

		assertEquals( 1, commander.getStartInitCallsCount() );
	}
}
