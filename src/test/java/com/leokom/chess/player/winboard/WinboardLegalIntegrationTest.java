package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test cooperation between Winboard &amp; LegalPlayer
 * Author: Leonid
 * Date-time: 07.02.16 22:44
 */
public class WinboardLegalIntegrationTest {private WinboardCommunicator communicator;
	private WinboardCommander commander;
	private LegalPlayer opponent;
	private WinboardPlayer playerSpy;

	@Before
	public void prepare() {
		communicator = mock( WinboardCommunicator.class );
		commander = new WinboardCommanderImpl( communicator );
		WinboardPlayer player = new WinboardPlayer();
		playerSpy = spy( player );
		playerSpy.initCommander( commander );

		opponent = new LegalPlayer();
		this.playerSpy.setOpponent( opponent );
		opponent.setOpponent( this.playerSpy );
	}

	@Test
	public void noMovesBeforeGo() {
		when( communicator.receive() ).thenReturn( "new" ).thenReturn( "force" );

		commander.processInputFromServer();
		commander.processInputFromServer();

		verify( playerSpy, times( 0 ) ).opponentMoved( any( Move.class ) );
	}

	@Ignore( "till fixed" )
	@Test
	public void noCallToGoNothingInReturnExpected() {
		when( communicator.receive() )
				.thenReturn( "new" )
				.thenReturn( "force" )
				.thenReturn( "usermove b1c3" );

		//TODO: how to avoid calling multiple times?
		commander.processInputFromServer();
		commander.processInputFromServer();
		commander.processInputFromServer();

		verify( playerSpy, never() ).opponentMoved( any( Move.class ) );
	}

	@Ignore( "till fixed" )
	@Test
	public void crashCase() {
		when( communicator.receive() )
			.thenReturn( "new" )
			.thenReturn( "force" )
			.thenReturn( "usermove b1c3" )
			.thenReturn( "go" )
			.thenReturn( "usermove g1f3" );

		//TODO: how to avoid calling multiple times?
		commander.processInputFromServer();
		commander.processInputFromServer();
		commander.processInputFromServer();
		commander.processInputFromServer();
		commander.processInputFromServer();
	}

}
