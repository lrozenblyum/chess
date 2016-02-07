package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Test cooperation between Winboard &amp; LegalPlayer
 * Author: Leonid
 * Date-time: 07.02.16 22:44
 */
public class WinboardLegalIntegrationTest {private WinboardCommunicator communicator;
	private WinboardCommander commander;
	private WinboardPlayer player;
	private LegalPlayer opponent;

	@Before
	public void prepare() {
		communicator = mock( WinboardCommunicator.class );
		commander = new WinboardCommanderImpl( communicator );
		player = spy( new WinboardPlayer( commander ) );
		opponent = new LegalPlayer();
		player.setOpponent( opponent );
		opponent.setOpponent( player );
	}

	@Test
	public void noMovesBeforeGo() {
		when( communicator.receive() ).thenReturn( "new" ).thenReturn( "force" );

		commander.processInputFromServer();

		verify( player, times( 0 ) ).opponentMoved( any( Move.class ) );
	}

}
