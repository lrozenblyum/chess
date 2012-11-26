package com.leokom.chess.gui.winboard;

import com.leokom.chess.framework.Player;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Check Winboard controller behaviour in combination with real
 * commander
 *
 * The only thing we allow to mock here is low-level communicator
 *
 * Author: Leonid
 * Date-time: 10.11.12 22:03
 */
public class WinBoardPlayerIntegrationTest {
	@Test
	public void switchesWinboardToSetUpMode() {
		//The commander mock is actually EMULATOR OF Winboard behaviour!
		//TODO: think about Mockito usage?
		MockCommunicatorSend communicatorSend = new MockCommunicatorSend();

		final Player player = new WinboardPlayer( new WinboardCommanderImpl( communicatorSend ) );

		assertEquals( 1, communicatorSend.getSentCommands().size() );
		final String initializationString = "feature done=0";
		assertEquals( initializationString, communicatorSend.getSentCommands().get( 0 ) );
	}
}
