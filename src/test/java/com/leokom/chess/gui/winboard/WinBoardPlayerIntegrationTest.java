package com.leokom.chess.gui.winboard;

import com.leokom.chess.player.DrawOfferedListener;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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


	@Test
	public void offerDrawListenerCalled() {
		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );

		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );

		final DrawOfferedListener listenerToCall = mock( DrawOfferedListener.class );
		player.onDrawOffered( listenerToCall );

		//low-level
		when( communicator.receive() ).thenReturn( "draw" );
		//mid-level processing
		commander.processInputFromServer();
		//top-level component has set up the commander's listener correctly
		verify( listenerToCall ).opponentOfferedDraw();
	}
}