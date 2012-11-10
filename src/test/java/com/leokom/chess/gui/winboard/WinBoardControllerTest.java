package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Controller;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
		MockCommunicator commanderMock = new MockCommunicator();

		final Controller controller = new WinboardController( commanderMock );

		assertEquals( 1, commanderMock.getSentCommands().size() );
		final String initializationString = "feature done=0";
		assertEquals( initializationString, commanderMock.getSentCommands().get( 0 ) );
	}

}
