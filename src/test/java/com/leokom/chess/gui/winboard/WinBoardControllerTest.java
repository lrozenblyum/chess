package com.leokom.chess.gui.winboard;

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
		//TODO: this will be not needed when controller doesn't depend on it...
		MockCommunicatorSend communicatorSend = new MockCommunicatorSend();

		MockCommander commander = new MockCommander();

		WinboardController controller = new WinboardController( communicatorSend, commander );

		assertEquals( 1, commander.getStartInitCallsCount() );
	}
}
