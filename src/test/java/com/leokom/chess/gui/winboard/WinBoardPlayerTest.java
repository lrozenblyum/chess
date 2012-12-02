package com.leokom.chess.gui.winboard;

import com.leokom.chess.framework.Player;
import com.leokom.chess.gui.Communicator;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 19.08.12 18:16
 */
public class WinBoardPlayerTest {
	private static final int PROTOCOL_VERSION = 2; //any??
	private static final int WAIT_TILL_QUIT = 5000;

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
	@Test( timeout = WAIT_TILL_QUIT )
	public void useCommanderForQuitCommand() {
		final Communicator quitCommunicator = mock( Communicator.class );
		stub( quitCommunicator.receive() ).toReturn( "quit" );

		WinboardCommander commander = new WinboardCommanderImpl( quitCommunicator );

		WinboardPlayer controller = new WinboardPlayer(	commander );

		controller.run();

	}

	@Test
	public void quitCommandSwitchesShutdownNeed() {
		WinboardCommander commander = mock( WinboardCommander.class );

		WinboardPlayer controller = new WinboardPlayer(	commander );
		ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		verify( commander ).setQuitListener( quitListener.capture() );

		assertFalse( controller.needShuttingDown() );

		//correct quit listener must enable need of shutting down
		quitListener.getValue().execute();

		assertTrue( controller.needShuttingDown() );
	}

	//ensure need of refactoring into commander instead of communicator
	@Test( timeout = 5000 )
	public void useCommanderForQuitCommandRealTest() {
		//dummy implementation - each time anybody sets a protover listener -
		//we quit IMMEDIATELY
		final WinboardCommander commander = mock( WinboardCommander.class );

		final ArgumentCaptor<QuitListener> quitListener = ArgumentCaptor.forClass( QuitListener.class );
		final Player winboardPlayer = new WinboardPlayer( commander );
		verify( commander ).setQuitListener( quitListener.capture() );

		doAnswer( new Answer() {
			@Override
			public Object answer( InvocationOnMock invocationOnMock ) {
				quitListener.getValue().execute();
				return null;  //just for compiler... due to generic Answer interface
			}
		} ).when( commander ).processInputFromServer();

		winboardPlayer.run();
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

		InOrder orderedCalls = inOrder( commander );

		orderedCalls.verify( commander ).enableUserMovePrefixes();
		orderedCalls.verify( commander ).finishInit();
	}
}
