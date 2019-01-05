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

	/*
	1)
	"offer draw" should also be used to claim 50-move ... draws that will occur after your move,
	by sending it before making the move.
	WinBoard will grant draw offers without the opponent having any say in it in situations where draws can be claimed.
	Only if the draw cannot be claimed, the offer will be passed to your opponent after you make your next move, just before WinBoard relays this move to the opponent.

	2) RESULT {COMMENT} When your engine detects that the game has ended by rule,
		your engine must output a line of the form "RESULT {comment}" (without the quotes),
		where RESULT is a PGN result code (1-0, 0-1, or 1/2-1/2), and comment is the reason.
		Here "by rule" means that the game is definitely over because of what happened on the board.
	* In normal chess, this includes ... the 50 move rule ...
	* This command should thus not be used to ... make draw-by-rule claims that are not yet valid in the current position
	* (but will be after you move). For offering and claiming such draws, "offer draw" should be used.
	 */
	@Test
	public void claimDraw() {
		commander.claimDrawByMovesCount( 50 );
		verify( communicator ).send( "1/2-1/2 {Draw claimed by 50 moves rule}" );
	}

	@Test
	public void illegalMove() {
		commander.illegalMove( "e2e7" );
		verify( communicator ).send( "Illegal move: e2e7" );
	}
}
