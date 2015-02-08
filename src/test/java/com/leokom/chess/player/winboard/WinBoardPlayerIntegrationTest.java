package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

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
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		//low-level
		when( communicator.receive() ).thenReturn( "draw" );
		//mid-level processing
		commander.processInputFromServer();
		//top-level component has set up the commander's listener correctly
		verify( opponent ).opponentOfferedDraw();
	}

	@Test
	public void resignListenerCalled() {
		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );

		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		//low-level

		//TODO: it's too implementation-specific, isn't it?

		when( communicator.receive() ).thenReturn( "result 1-0 {Black resigns}" );
		//mid-level processing
		commander.processInputFromServer();
		//top-level component has set up the commander's listener correctly
		verify( opponent ).opponentResigned();
	}

	@Test
	public void userMoveNoException() {
		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );

		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );
		player.setOpponent( mock( Player.class ) );

		//low-level
		when( communicator.receive() ).thenReturn( "usermove e2e4" );
		//mid-level processing
		commander.processInputFromServer();
		//no exceptions expected
	}

	//Winboard -> Player
 	@Test
	public void promotionCorrectlyTranslatedToCommonStandard() {

		assertTranslationOfReceivedCommandToMoveForOpponent(
				"usermove f7g8q", "f7", "g8Q" );
	}

	//Player -> Winboard
	@Test
	public void promotionCorrectlyTranslatedFromCommonStandard() {
		assertTranslationOfCommandFromPlayerToWinboardClient(
				new Move( "f7", "f8Q" ), "move f7f8q" );
	}

	@Test
	public void castlingCorrectlyTranslatedToPlayer() {
		assertTranslationOfReceivedCommandToMoveForOpponent(
				"usermove e1g1", "e1", "g1" );
	}

	@Test
	public void castlingCorrectlyTranslatedToWinboardClient() {
		assertTranslationOfCommandFromPlayerToWinboardClient(
				new Move( "e8", "c8" ), "move e8c8" );
	}

	@Test
	public void checkmateFromPlayerToWinboard() {
		//implementing fool's mate

		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );
		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );

		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		fail( "Not implemented yet" );
		//TODO:
		//1. f3 e5
		//2. g4?? Qh4#
	}

	private void assertTranslationOfCommandFromPlayerToWinboardClient( Move playerMove, String commandSentToWinboardClient ) {
		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );
		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );

		//TODO: I don't like this reset, but player sends several commands
		//like set features etc, how to do better?
		Mockito.reset( communicator );

		player.opponentMoved( playerMove );


		verify( communicator ).send( commandSentToWinboardClient );
	}

	private void assertTranslationOfReceivedCommandToMoveForOpponent( String winboardReceivedMessage, String squareFrom, String destination ) {
		final WinboardCommunicator communicator = mock( WinboardCommunicator.class );
		final WinboardCommander commander = new WinboardCommanderImpl( communicator );
		final WinboardPlayer player = new WinboardPlayer( commander );
		Player opponent = mock( Player.class );

		player.setOpponent( opponent );

		when( communicator.receive() ).thenReturn( winboardReceivedMessage );

		commander.processInputFromServer();

		verify( opponent ).opponentMoved( new Move( squareFrom, destination ) );
	}
}