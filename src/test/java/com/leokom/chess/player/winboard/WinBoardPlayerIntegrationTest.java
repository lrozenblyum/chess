package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.*;
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
		verify( opponent ).opponentMoved( Move.OFFER_DRAW );
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

	//To accept the draw, send "offer draw".
	@Test
	public void informWinboardAboutAcceptDraw() {
		assertTranslationOfCommandFromPlayerToWinboardClient(
				Move.ACCEPT_DRAW, "offer draw" );
	}

	//If there was a draw for some non-obvious reason,
	// perhaps your opponent called your flag when he had insufficient mating material
	// (or vice versa), or perhaps the operator agreed to a draw manually.
	@Test
	public void informOpponentAboutAcceptDraw() {
		assertTranslationOfReceivedCommandToMoveForOpponent(
				"result 1/2-1/2 {some maybe impl. specific message}", Move.ACCEPT_DRAW );
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
				"usermove f7g8q", new Move( "f7", "g8Q" ) );
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
				"usermove e1g1", new Move( "e1", "g1" ) );
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

		verify( communicator ).send( "1-0 {Checkmate}" );
		verify( communicator, never() ).send( "0-1 {Checkmate}" );
	}

	@Test
	public void obligatoryDraw() {
		player.setPosition( Position.getInitialPosition( new RulesBuilder().movesTillDraw( 1 ).build() ) );

		final WinboardTestGameBuilder builder = new WinboardTestGameBuilder( player, communicator );
			builder
			.move( new Move( "b1", "c3" ) )
			.move( new Move( "b8", "a6" ) )
			.play();

		verify( communicator, atLeastOnce() ).send( startsWith( "1/2-1/2" ) );
	}

	@Test
	public void stalemateSent() {
		final Position position = new PositionBuilder()
				.add( Side.WHITE, "d5", PieceType.KING )
				.add( Side.WHITE, "d7", PieceType.PAWN )
				.add( Side.BLACK, "d8", PieceType.KING )
				.build();

		player.setPosition( position );
		final WinboardTestGameBuilder builder = new WinboardTestGameBuilder( player, communicator );

		builder.move( new Move( "d5", "d6" ) ).play();

		verify( communicator ).send( startsWith( "1/2-1/2" ) );
		verify( communicator ).send( contains( "Stalemate" ) );
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

		verify( communicator ).send( "0-1 {Checkmate}" );
		verify( communicator, never() ).send( "1-0 {Checkmate}" );
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

	private void assertTranslationOfReceivedCommandToMoveForOpponent( String winboardReceivedMessage, Move move ) {
		Player opponent = mock( Player.class );

		player.setOpponent( opponent );

		when( communicator.receive() ).thenReturn( winboardReceivedMessage );

		commander.processInputFromServer();

		verify( opponent ).opponentMoved( move );
	}
}