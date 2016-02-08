package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.legalMover.LegalPlayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Test cooperation between Winboard &amp; LegalPlayer
 * Author: Leonid
 * Date-time: 07.02.16 22:44
 */
public class WinboardLegalIntegrationTest {private WinboardCommunicator communicator;
	private WinboardCommander commander;
	private WinboardPlayer playerSpy;

	@Before
	public void prepare() {
		communicator = mock( WinboardCommunicator.class );
		commander = new WinboardCommanderImpl( communicator );
		WinboardPlayer player = new WinboardPlayer();
		playerSpy = spy( player );
		playerSpy.initCommander( commander );

		LegalPlayer opponent = new LegalPlayer();
		this.playerSpy.setOpponent( opponent );
		opponent.setOpponent( this.playerSpy );
	}

	@Test
	public void noMovesBeforeGo() {
		simulateWinboard( "new", "force" );

		verify( playerSpy, times( 0 ) ).opponentMoved( any( Move.class ) );
	}

	@Test
	public void noCallToGoNothingInReturnExpected() {
		simulateWinboard( "new", "force", "usermove b1c3" );

		verify( playerSpy, never() ).opponentMoved( any( Move.class ) );
	}

	@Ignore( "till fixed" )
	@Test
	public void crashCase() {
		simulateWinboard( "new", "force", "usermove b1c3", "go", "usermove g1f3" );
	}

	//smth similar to WinboardTestGameBuilder but simpler
	private void simulateWinboard( String... commandsFromWinboard ) {
		//programming communicator
		OngoingStubbing<String> stubbing = when( communicator.receive() );
		for( String command : commandsFromWinboard ) {
			stubbing = stubbing.thenReturn( command );
		}

		//calling Commander to fetch communicator and then inform Player
		Arrays.stream( commandsFromWinboard )
				.forEach( command -> commander.processInputFromServer() );
	}

}
