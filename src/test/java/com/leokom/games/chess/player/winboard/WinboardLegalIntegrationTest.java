package com.leokom.games.chess.player.winboard;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.PieceType;
import com.leokom.games.chess.player.legal.LegalPlayer;
import com.leokom.games.chess.player.legal.brain.common.Brain;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test cooperation between Winboard &amp; LegalPlayer
 * Author: Leonid
 * Date-time: 07.02.16 22:44
 */
public class WinboardLegalIntegrationTest {private WinboardCommunicator communicator;
	private WinboardCommander commander;
	private WinboardPlayer playerSpy;
	private LegalPlayer opponent;

	@Before
	public void prepare() {
		communicator = mock( WinboardCommunicator.class );
		commander = new WinboardCommanderImpl( communicator );
		//the constructor initializes not needed commander in default constructor and then injects another one,
		//it should be improved in https://github.com/lrozenblyum/chess/issues/354
		WinboardPlayer player = new WinboardPlayer();
		playerSpy = spy( player );
		playerSpy.initCommander( commander );

		setOpponent( new LegalPlayer() );
	}

	private void setOpponent( LegalPlayer opponent ) {
		this.opponent = opponent;
		this.playerSpy.setOpponent( opponent );
		this.opponent.setOpponent( this.playerSpy );
	}

	@Test
	public void legalPlayerCanResignWhenNotHisMove() {
		Brain brain = mock( Brain.class );
		when( brain.findBestMoveForOpponent( any() ) ).thenReturn(Move.RESIGN);
		LegalPlayer legalPlayer = new LegalPlayer(brain);
		setOpponent( legalPlayer );

		//winboard playing white, suggests draw
		simulateWinboard( "new", "draw" );

		verify( playerSpy ).opponentMoved( Move.RESIGN );
	}

	@Test
	public void winboardUnderstandsMultipleMoveWithOfferDraw() {
		Brain brain = mock( Brain.class );
		when( brain.findBestMove( any() ) ).thenReturn(Arrays.asList(
				new Move( "d7", "d5" ),
				Move.OFFER_DRAW )
		);
		LegalPlayer legalPlayer = new LegalPlayer(brain);
		setOpponent( legalPlayer );

		simulateWinboard( "new", "usermove g1f3" );

		//testing indirectly, if accept draw is allowed that means we respected offer draw
		assertThat( playerSpy.getPosition().getMoves(), hasItem( Move.ACCEPT_DRAW ) );
	}

	@Test
	public void winboardUnderstandsMultipleMoveWithResign() {
		Brain brain = mock( Brain.class );
		when( brain.findBestMove( any() ) ).thenReturn(Arrays.asList(
			new Move( "e7", "e5" ),
			Move.RESIGN )
		);
		LegalPlayer legalPlayer = new LegalPlayer(brain);
		setOpponent( legalPlayer );

		simulateWinboard( "new", "usermove b1c3" );

		assertTrue( playerSpy.getPosition().isTerminal() );
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

	@Test
	public void crashCase() {
		simulateWinboard( "new", "force", "usermove b1c3", "go", "usermove g1f3" );
	}

	@Test
	public void positionUpdatedUsually() {
		simulateWinboard( "new", "usermove b1c3" );
		Assert.assertEquals( PieceType.KNIGHT,opponent.getPosition().getPieceType( "c3" ) );
		Assert.assertNull( opponent.getPosition().getPieceType( "b1" ) );
	}

	@Test
	public void positionResetByNew() {
		simulateWinboard( "new", "usermove b1c3", "new" );
		Assert.assertEquals( PieceType.KNIGHT,opponent.getPosition().getPieceType( "b1" ) );
	}

	//by specification : new command should cause leave of force mode
	@Test
	public void leaveForceModeByNew() {
		simulateWinboard( "new", "force", "new" );
		Assert.assertFalse( opponent.isRecordingMode() );
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
