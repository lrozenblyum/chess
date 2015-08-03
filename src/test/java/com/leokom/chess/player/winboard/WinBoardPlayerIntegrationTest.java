package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.PositionBuilder;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Check Winboard player behaviour in combination with real
 * commander
 *
 * The only thing we allow to mock here is low-level communicator
 *
 * Author: Leonid
 * Date-time: 10.11.12 22:03
 */
public class WinBoardPlayerIntegrationTest {

	private WinboardCommunicator communicator;
	private WinboardCommander commander;
	private WinboardPlayer player;

	@Before
	public void prepare() {
		communicator = mock( WinboardCommunicator.class );
		commander = new WinboardCommanderImpl( communicator );
		player = new WinboardPlayer( commander );
	}

	@Test
	public void switchesWinboardToSetUpMode() {
		//The commander mock is actually EMULATOR OF Winboard behaviour!
		verify( communicator ).send( "feature done=0" );
	}


	@Test
	public void offerDrawListenerCalled() {
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		//low-level
		when( communicator.receive() ).thenReturn( "draw" );
		//mid-level processing
		commander.processInputFromServer();
		//top-level component has set up the commander's listener correctly
		verify( opponent ).opponentOfferedDraw();
	}

	//resign
	//If your engine wants to resign, it can send the command "resign". Alternatively, it can use the "RESULT
	//{comment}" command if the string "resign" is included in the comment; for example "0-1 {White
	//	resigns}". xboard relays the resignation to the user, the ICS, the other engine in Two Machines mode,
	//and the PGN save file as required. Note that many interfaces work more smoothly if you resign before
	//you move.
	@Test
	public void informWinboardAboutResign() {
		//I'm choosing a more specific way to inform Winboard
		//about resigning (string resign)

		assertTranslationOfCommandFromPlayerToWinboardClient(
				Move.RESIGN, "resign" );
	}

	@Test
	public void resignListenerCalled() {
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		//low-level

		//TODO: it's too implementation-specific, isn't it?

		when( communicator.receive() ).thenReturn( "result 1-0 {Black resigns}" );
		//mid-level processing
		commander.processInputFromServer();
		//top-level component has set up the commander's listener correctly
		verify( opponent ).opponentMoved( Move.RESIGN );
	}

	@Test
	public void userMoveNoException() {
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
		PositionBuilder position = new PositionBuilder()
				.addPawn( Side.WHITE, "f7" )
				.setSideOf( "f7" );

		player.setPosition( position.build() );

		assertTranslationOfReceivedCommandToMoveForOpponent(
				"usermove f7g8q", "f7", "g8Q" );
	}

	//Player -> Winboard
	@Test
	public void promotionCorrectlyTranslatedFromCommonStandard() {
		PositionBuilder position = new PositionBuilder()
				.addPawn( Side.WHITE, "f7" )
				.setSideOf( "f7" );

		player.setPosition( position.build() );

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
		PositionBuilder position = new PositionBuilder()
				.add( Side.BLACK, "e8", PieceType.KING )
				.add( Side.BLACK, "a8", PieceType.ROOK );

		player.setPosition( position.setSideOf( "e8" ).build() );

		assertTranslationOfCommandFromPlayerToWinboardClient(
				new Move( "e8", "c8" ), "move e8c8" );
	}

	@Test
	public void checkmateWhenWinboardWins() {
		//implementing fool's mate

		new WinboardTestGameBuilder( player, communicator )
				.move( new Move( "e2", "e4" ) )
				.move( new Move( "g7", "g5" ) )
				.move( new Move( "b1", "c3" ) )
				.move( new Move( "f7", "f5" ) )
				.move( new Move( "d1", "h5" ) )
				.play();

		verify( communicator ).send( "1-0 {LeokomChess : checkmate}" );
		verify( communicator, never() ).send( "0-1 {LeokomChess : checkmate}" );
	}

	//Winboard vs Player (White vs Black)
	//1. f3 e5
	//2. g4?? Qh4#
	//need to inform UI that it lost (and Player won!)
	@Test
	public void checkmateFromPlayerToWinboard() {
		//implementing fool's mate

		new WinboardTestGameBuilder( player, communicator )
		.move( new Move( "f2", "f3" ) )
		.move( new Move( "e7", "e5" ) )
		.move( new Move( "g2", "g4" ) )
		.move( new Move( "d8", "h4" ) )
		.play();

		verify( communicator ).send( "0-1 {LeokomChess : checkmate}" );
		verify( communicator, never() ).send( "1-0 {LeokomChess : checkmate}" );
	}

	@Test
	public void resignFromPlayerToWinboard() {
		new WinboardTestGameBuilder( player, communicator )
				.move( new Move( "f2", "f3" ) )
				.move( new Move( "e7", "e5" ) )
				.move( new Move( "g2", "g4" ) )
				.move( Move.RESIGN )
				.play();

		verify( communicator ).send( "resign" );
		verify( communicator, never() ).send( "1-0 {LeokomChess : checkmate}" );
	}

	@Test
	public void offerDrawFromPlayerToWinboard() {
		new WinboardTestGameBuilder( player, communicator )
				.move( new Move( "f2", "f3" ) )
				.move( new Move( "e7", "e5" ) )
				.move( new Move( "g2", "g4" ) )
				.move( Move.OFFER_DRAW )
				.play();

		verify( communicator ).send( "offer draw" );
	}

	@Test
	public void resignFromWinboardToPlayer() {
		final WinboardTestGameBuilder builder = new WinboardTestGameBuilder( player, communicator );
				builder
				.move( new Move( "f2", "f3" ) )
				.move( new Move( "e7", "e5" ) )
				.move( Move.RESIGN )
				.play();

		verify( builder.getOpponent() ).opponentMoved( Move.RESIGN );
	}

	@Test
	public void noFalsePositiveResign() {
		final WinboardTestGameBuilder builder = new WinboardTestGameBuilder( player, communicator );
			builder
				.move( new Move( "f2", "f3" ) )
				.move( new Move( "e7", "e5" ) )
				.move( new Move( "g2", "g4" ) )
				.move( new Move( "d8", "h4" ) )
				//realistic result of game -> xboard sends result
				.customWinboardResponse( "result 0-1 {Black wins}" )
				.play();

		verify( builder.getOpponent(), never() ).opponentMoved( Move.RESIGN );
	}

	@Test
	public void noFalsePositiveResignWhenWhiteWins() {
		final WinboardTestGameBuilder builder = new WinboardTestGameBuilder( player, communicator );
		builder
				.move( new Move( "e2", "e4" ) )
				.move( new Move( "g7", "g5" ) )
				.move( new Move( "d2", "d4" ) )
				.move( new Move( "f7", "f6" ) )
				.move( new Move( "d1", "h5" ) )
				//realistic result of game -> xboard sends result
				.customWinboardResponse( "result 1-0 {White wins}" )
				.play();

		verify( builder.getOpponent(), never() ).opponentMoved( Move.RESIGN );
	}

	@Test
	public void noCheckmateNoFalseInforming() {
		//implementing fool's mate
		final Player opponent = mock( Player.class );
		player.setOpponent( opponent );

		new WinboardTestGameBuilder( player, communicator )
				.move( new Move( "f2", "f3" ) )
				.move( new Move( "e7", "e5" ) )
				.play();

		verify( communicator, never() ).send( "0-1 {LeokomChess : checkmate}" );
	}

	private void assertTranslationOfCommandFromPlayerToWinboardClient( Move playerMove, String commandSentToWinboardClient ) {
		player.opponentMoved( playerMove );
		verify( communicator ).send( commandSentToWinboardClient );
	}

	private void assertTranslationOfReceivedCommandToMoveForOpponent( String winboardReceivedMessage, String squareFrom, String destination ) {
		Player opponent = mock( Player.class );

		player.setOpponent( opponent );

		when( communicator.receive() ).thenReturn( winboardReceivedMessage );

		commander.processInputFromServer();

		verify( opponent ).opponentMoved( new Move( squareFrom, destination ) );
	}
}