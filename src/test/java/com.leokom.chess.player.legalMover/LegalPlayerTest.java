package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * Author: Leonid
 * Date-time: 09.12.13 22:01
 */
public class LegalPlayerTest {
	@Test
	public void legalPlayerCreation() {
		new LegalPlayer( Side.WHITE );
	}

	@Test
	public void legalPlayerExecutesSingleAllowedMove() {
		Player opponent = mock( Player.class );

		//assuming playing as white...
		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		Position position = new Position( null );
		position.add( Side.WHITE, "a1", PieceType.KING );
		position.add( Side.BLACK, "c2", PieceType.KING );

		player.setPosition( position );

		//TODO: it's overhead but in current architecture
		//the only way to inform another player about a move
		//is as a reaction to 'opponentMoved'


		player.opponentMoved( "c2c1" );
		//leaving for whites only single move:
		//a1-a2

		verify( opponent ).opponentMoved( "a1a2" );
	}

	//assuming playing as white...   (still!)
	//I'm not ready to triangulate the sides change
	@Test
	public void legalPlayerExecutesSingleAllowedMoveTriangulate() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		Position position = new Position( null );
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g5", PieceType.KING );

		player.setPosition( position );

		//TODO: overhead (see another test for description)
		player.opponentMoved( "g5g6" );
		//leaving for whites only single move:

		verify( opponent ).opponentMoved( "h8g8" );
	}

	@Test
	public void legalPlayerCanMoveFirst() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		Position position = new Position( null );
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g6", PieceType.KING );

		player.setPosition( position );

		//TODO: ugly way to say: it's your first move now!
		player.opponentMoved( null );

		verify( opponent ).opponentMoved( "h8g8" );
	}

	@Test
	public void initialPositionPossibleMovement() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!

		//first check that at least some move is done.
		verify( opponent ).opponentMoved( anyString() );
	}

	@Test
	public void secondMoveCanAlsoBeDone() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!

		//first check that at least some move is done.
		verify( opponent ).opponentMoved( anyString() );

		player.opponentMoved( "e7e5" );

		//hmm twice because Mockito adds the invocation count
		//another option is reset call which is not recommended.
		verify( opponent, times( 2 ) ).opponentMoved( anyString() );
	}

	@Test
	public void secondMoveTriangulate() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!
		player.opponentMoved( "d7d5" );
	}

	@Test
	public void noCrashAfterKnightMove() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!
		player.opponentMoved( "g8f6" );
	}

	//leave just h8, h7 as a space for the King
	@Test
	public void proveNeedToUpdatePositionAfterOurMove() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.WHITE );
		player.setOpponent( opponent );

		final Position position = new Position( null );

		//white King surrounded
		position.add( Side.WHITE, "h8", PieceType.KING );
		position.add( Side.BLACK, "g8", PieceType.KNIGHT ); //protects h6
		position.add( Side.BLACK, "f8", PieceType.ROOK ); //protects g8

		position.add( Side.BLACK, "a6", PieceType.ROOK ); //protects g6

		position.add( Side.BLACK, "h6", PieceType.BISHOP ); //protects g7

		position.add( Side.BLACK, "a1", PieceType.KING );

		player.setPosition( position );

		player.opponentMoved( null );
		verify( opponent ).opponentMoved( "h8h7" );

		reset( opponent ); //NOT recommended by Mockito
		player.opponentMoved( "a1a2" );

		verify( opponent ).opponentMoved( "h7h8" );
	}

	@Test
	public void blackMoving() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer( Side.BLACK );
		player.setOpponent( opponent );

		final Position position = new Position( null );

		position.add( Side.WHITE, "c1", PieceType.KING );
		position.add( Side.BLACK, "a1", PieceType.KING );

		player.setPosition( position );

		player.opponentMoved( null ); //force our move

		verify( opponent ).opponentMoved( "a1a2" ); //proving the only move of blacks
	}

	//TODO: format issues: we support now Winboard format which isn't fine?
}
