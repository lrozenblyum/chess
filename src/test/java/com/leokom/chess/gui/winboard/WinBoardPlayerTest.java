package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardPlayerTest {
	private static final int PROTOCOL_VERSION = 2; //any??

	//this test should emulate WinBoard behaviour and analyze our reaction on it.
	//in theory in future we could extract some Winboard emulator

	@Test
	public void creationSwitchesToInitMode() {
		WinboardCommander commander = mock( WinboardCommander.class );

		//implicit call of startInit
		new WinboardPlayer( commander );

		//it really checks only 1 method call
		verify( commander ).startInit();
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommand() {
		Communicator quitCommunicator = MockCommunicatorReceiveCreator.getReceiveCommunicator( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardPlayer controller = new WinboardPlayer(
				commander );

		controller.run();
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommandRealTest() {
		//dummy implementation - each time anybody sets a protover listener -
		//we quit IMMEDIATELY
		WinboardCommander commander = new MockCommander() {
			@Override
			public void setQuitListener( final QuitListener quitListener ) {
				quitListener.execute();
			}
		};

		WinboardPlayer controller = new WinboardPlayer(	commander );

		controller.run();
	}

	/**
	 * validate that our WinboardPlayer
	 * in its constructor
	 * initializes the commander's protover listener
	 * in which, sets up our desired winboard properties
	 * and finishes the initialization
	 */
	@Test
	public void correctProtoverReaction() {
		//dummy implementation - each time anybody sets a protover listener -
		//we call it IMMEDIATELY
		WinboardCommander commander = mock( WinboardCommander.class );
		ArgumentCaptor< ProtoverListener > listenerCaptor = ArgumentCaptor.forClass( ProtoverListener.class );
		new WinboardPlayer(	commander );

		//the player must have set its listener in constructor...
		verify( commander ).setProtoverListener( listenerCaptor.capture() );

		//calling the protover listener - it must have implications described below.
		listenerCaptor.getValue().execute( PROTOCOL_VERSION );

		//TODO: it doesn't check methods order...
		verify( commander ).enableUserMovePrefixes();
		verify( commander ).finishInit();
	}
}
