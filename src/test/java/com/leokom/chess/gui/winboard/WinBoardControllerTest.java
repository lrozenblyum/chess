package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
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
	public void creationSwitchesToInitMode() {
		MockCommander commander = new MockCommander();

		WinboardController controller = new WinboardController( commander );

		assertEquals( 1, commander.getStartInitCallsCount() );
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommand() {
		Communicator quitCommunicator = MockCommunicatorReceiveCreator.getReceiveCommunicator( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardController controller = new WinboardController(
				commander );

		controller.run();
	}
}
