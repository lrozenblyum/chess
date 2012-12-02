package com.leokom.chess.gui.winboard;

import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
		WinboardCommunicator communicator = mock( WinboardCommunicator.class );

		new WinboardPlayer( new WinboardCommanderImpl( communicator ) );

		verify( communicator ).send( "feature done=0" );
	}
}