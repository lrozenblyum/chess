package com.leokom.chess.player.winboard;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:16
 */
public class WinBoardCommanderSendTest {
	private WinboardCommander commander;
	private Communicator communicator;

	@Before
	public void prepare() {
		communicator = mock( Communicator.class );
		commander = new WinboardCommanderImpl( communicator );
	}

	@Test
	public void noCommandsSendFromScratch() {
		verify( communicator, never() ).send( anyString() );
	}

	@Test
	public void initializationStarted() {
		commander.startInit();
		verify( communicator ).send( "feature done=0" );
	}

	@Test
	public void userMovesPrefixes() {
		commander.enableUserMovePrefixes();
		verify( communicator ).send( "feature usermove=1" );
	}

	@Test
	public void initializationFinished() {
		commander.finishInit();
		verify( communicator ).send( "feature done=1" );
	}

	@Test
	public void agreeToDraw() {
		commander.agreeToDrawOffer();
		verify( communicator ).send( "offer draw" );
	}

	@Test
	public void opponentMove() {
		commander.opponentMoved( "e2e4" );
		verify( communicator ).send( "move e2e4" );
	}

	@Test
	public void obligatoryDraw() {
		commander.obligatoryDrawByMovesCount( 75 );
		verify( communicator ).send( "1/2-1/2 {Draw by 75 moves rule}" );
	}

	@Test
	public void illegalMove() {
		commander.illegalMove();
		verify( communicator ).send( startsWith( "Illegal move" ) );
	}
}
