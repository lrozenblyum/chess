package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Commander;
import com.leokom.chess.gui.Controller;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
		MockCommander commanderMock = new MockCommander();

		final Controller controller = new WinboardController( commanderMock );

		assertEquals( 1, commanderMock.SENT_COMMANDS.size() );
		final String initializationString = "feature done=0";
		assertEquals( initializationString, commanderMock.SENT_COMMANDS.get( 0 ) );
	}

	private static class MockCommander implements Commander {
		private List<String> SENT_COMMANDS = new ArrayList<String>();

		@Override
		public void send( String command ) {
			SENT_COMMANDS.add( command );
		}

		@Override
		public String receive() {
			return null;
		}
	}
}
