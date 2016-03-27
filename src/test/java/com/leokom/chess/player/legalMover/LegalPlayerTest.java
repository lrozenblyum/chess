package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;
import com.leokom.chess.player.Player;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.Answer;

import java.util.OptionalInt;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 09.12.13 22:01
 */
public class LegalPlayerTest {

	private Player opponent;

	@Before
	public void setUp() throws Exception {
		opponent = mock( Player.class );
	}

	@Test
	public void legalPlayerCorrectWhenObligatoryDrawAchievedByOpponentMove() {
		Rules rules = mock( Rules.class );
		when( rules.getMovesTillDraw() ).thenReturn( OptionalInt.of( 1 ) );
		final Position position = Position.getInitialPosition( rules );

		final LegalPlayer player = getLegalPlayer();
		player.setPosition( position, Side.WHITE );
		player.setOpponent( opponent );

		//just single answer programmed, next - do nothing
		doAnswer( invocationOnMock -> { player.opponentMoved( new Move( "e7", "e5" ) ); return null; } )
		.doAnswer( invocation -> null )
		.when( opponent ).opponentMoved( any( Move.class ) );

		player.joinGameForSideToMove();
	}

	private static class EvaluatorToSpeedUpObligatoryDraw implements Evaluator {
		private static final double WORST_MOVE = 0.0;
		private static final double BEST_MOVE = 1.0;

		@Override
		public double evaluateMove( Position position, Move move ) {
			 if ( move.isSpecial() ) {
				 return WORST_MOVE;
			 }

			final Position result = position.move( move );
			if ( result.isTerminal() && result.getWinningSide() == null ) {
				return BEST_MOVE;
			}

			return WORST_MOVE;
		}
	}

	@Test
	public void legalPlayerCorrectWhenObligatoryDrawAchievedByHisMove() {
		Rules rules = mock( Rules.class );
		when( rules.getMovesTillDraw() ).thenReturn( OptionalInt.of( 1 ) );
		final Position position = Position.getInitialPosition( rules );

		final LegalPlayer player = new LegalPlayer( new EvaluatorToSpeedUpObligatoryDraw() );
		player.setPosition( position, Side.BLACK );
		player.setOpponent( opponent );
		player.opponentMoved( new Move( "e2", "e4" ) );
	}

	@Test
	public void legalPlayerCreation() {
		getLegalPlayer();
	}

	@Test
	public void legalPlayerExecutesSingleAllowedMove() {

		//assuming playing as white...
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		Position position = new Position( Side.BLACK );
		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.BLACK, "c2", PieceType.KING );

		player.setPosition( position, Side.WHITE );

		player.opponentMoved( new Move( "c2", "c1" ) );
		//leaving for whites only single move:
		//a1-a2

		verify( opponent ).opponentMoved( new Move( "a1", "a2" ) );
	}

	private LegalPlayer getLegalPlayer() {
		return new LegalPlayer( new MasterEvaluatorTweaked() );
	}

	//assuming playing as white...   (still!)
	//I'm not ready to triangulate the sides change
	@Test
	public void legalPlayerExecutesSingleAllowedMoveTriangulate() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		Position position = new Position( Side.BLACK );
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g5", PieceType.KING );

		player.setPosition( position, Side.WHITE );

		player.opponentMoved( new Move( "g5", "g6" ) );
		//leaving for whites only single move:

		verify( opponent ).opponentMoved( new Move( "h8", "g8" ) );
	}

	@Test
	public void legalPlayerCanMoveFirstAfterRun() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		player.opponentSuggestsMeStartNewGameWhite();

		verify( opponent ).opponentMoved( any( Move.class ) );
	}

	@Test
	public void legalPlayerCanMoveFirst() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g6", PieceType.KING );

		player.setPosition( position, Side.WHITE );

		player.executeOurMove();

		verify( opponent ).opponentMoved( new Move( "h8", "g8" ) );
	}

	@Test
	public void initialPositionPossibleMovement() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		player.opponentSuggestsMeStartNewGameWhite(); //our first move!

		//first check that at least some move is done.
		verify( opponent ).opponentMoved( any( Move.class ) );
	}

	@Test
	public void secondMoveCanAlsoBeDone() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		player.opponentSuggestsMeStartNewGameWhite(); //our first move!

		//first check that at least some move is done.
		verify( opponent ).opponentMoved( any( Move.class ) );

		player.opponentMoved( new Move( "e7", "e5" ) );

		//hmm twice because Mockito adds the invocation count
		//another option is reset call which is not recommended.
		verify( opponent, times( 2 ) ).opponentMoved( any( Move.class ) );
	}

	@Test
	public void secondMoveTriangulate() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		player.opponentSuggestsMeStartNewGameWhite();
		player.opponentMoved( new Move( "d7", "d5" ) );
	}

	@Test
	public void noCrashAfterKnightMove() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		player.opponentSuggestsMeStartNewGameWhite();
		player.opponentMoved( new Move( "g8", "f6" ) );
	}

	//leave just h8, h7 as a space for the King
	@Test
	public void proveNeedToUpdatePositionAfterOurMove() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		Position position = new Position( Side.WHITE );

		//white King surrounded
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g8", PieceType.KNIGHT ); //protects h6
		position.add( Side.BLACK, "f8", PieceType.ROOK ); //protects g8

		position.add( Side.BLACK, "a6", PieceType.ROOK ); //protects g6

		position.add( Side.BLACK, "h6", PieceType.BISHOP ); //protects g7

		position.add( Side.BLACK, "a1", PieceType.KING );

		player.setPosition( position, Side.WHITE );

		player.executeOurMove();
		verify( opponent ).opponentMoved( new Move( "h8", "h7" ) );

		reset( opponent ); //NOT recommended by Mockito
		player.opponentMoved( new Move( "a1", "a2" ) );

		verify( opponent ).opponentMoved( new Move( "h7", "h8" ) );
	}

	//let another player respond immediately inside reaction to our move
	@Test
	public void proveNeedToUpdatePositionAfterOurMoveInRecursiveCase() {
		final LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		final Position position = new Position( Side.WHITE );

		//white King surrounded
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g8", PieceType.KNIGHT ); //protects h6
		position.add( Side.BLACK, "f8", PieceType.ROOK ); //protects g8

		position.add( Side.BLACK, "a6", PieceType.ROOK ); //protects g6

		position.add( Side.BLACK, "h6", PieceType.BISHOP ); //protects g7

		position.add( Side.BLACK, "a1", PieceType.KING );

		player.setPosition( position, Side.WHITE );

		doAnswer( getAnswerToH8H7( player ) ).when( opponent ).opponentMoved( new Move( "h8", "h7" ) );

		player.executeOurMove(); //results in LegalPlayer h8h7

		verify( opponent ).opponentMoved( new Move( "h8", "h7" ) );
	}

	private Answer<?> getAnswerToH8H7( final LegalPlayer player ) {
		return invocationOnMock -> {
			player.opponentMoved( new Move( "a1", "a2" ) );
			return null;
		};
	}

	@Test
	public void blackMoving() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		final Position position = new Position( Side.WHITE );

		position.add( Side.WHITE, "d1", PieceType.KING );
		position.add( Side.BLACK, "a1", PieceType.KING );

		player.setPosition( position, Side.BLACK );

		player.opponentMoved( new Move( "d1", "c1" ) );

		verify( opponent ).opponentMoved( new Move( "a1", "a2" ) ); //proving the only move of blacks
	}

	//TODO: technically opponent can accept/reject draw offer
	//this is not implemented yet
	//this test is to ensure we won't start moving when opponent
	//offered draw but didn't move
	@Test
	public void noMovementWhenOfferDraw() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		final Position position = new Position( Side.WHITE );
		position.add( Side.WHITE, "d1", PieceType.KING );
		position.add( Side.BLACK, "a1", PieceType.KING );
		player.setPosition( position, Side.BLACK );

		player.opponentMoved( Move.OFFER_DRAW );

		verify( opponent, never() ).opponentMoved( any( Move.class ) );
	}

	//e.g. Winboard is WHITE initially (Legal = BLACK)
	//WInboard suggests starting new game where LEGAL = WHITE, Winboard = BLACK
	//we should reinstall state of the game
	@Test
	public void legalPlayerCanStartNewGameInMiddleOfExisting() {
		LegalPlayer player = getLegalPlayer();
		player.setOpponent( opponent );

		final Position position = new Position( Side.WHITE );
		//pieces not on initial position to make sure no 'accident'
		//correct move
		position.add( Side.WHITE, "d1", PieceType.KING );
		position.add( Side.BLACK, "a1", PieceType.KING );
		player.setPosition( position, Side.WHITE );

		player.opponentSuggestsMeStartNewGameWhite();

		ArgumentCaptor<Move> legalPlayerMove = ArgumentCaptor.forClass( Move.class );
		verify( opponent ).opponentMoved( legalPlayerMove.capture() );

		assertTrue( "Legal player must play legally after switch of sides. Actual move: " + legalPlayerMove.getValue(),
				Position.getInitialPosition().getMoves().stream()
				.anyMatch( legalPlayerMove.getValue()::equals ) );
	}


	//TODO: format issues: we support now Winboard format which isn't fine?
}
