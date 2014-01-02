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
		new LegalPlayer();
	}

	@Test
	public void legalPlayerExecutesSingleAllowedMove() {
		Player opponent = mock( Player.class );

		//assuming playing as white...
		LegalPlayer player = new LegalPlayer();
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

		LegalPlayer player = new LegalPlayer();
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

		LegalPlayer player = new LegalPlayer();
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

		LegalPlayer player = new LegalPlayer();
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!

		//first check that at least some move is done.
		verify( opponent ).opponentMoved( anyString() );
	}

	@Test
	public void secondMoveCanAlsoBeDone() {
		Player opponent = mock( Player.class );

		LegalPlayer player = new LegalPlayer();
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

		LegalPlayer player = new LegalPlayer();
		player.setOpponent( opponent );

		player.opponentMoved( null ); //our first move!
		player.opponentMoved( "d7d5" );
	}

	//TODO: format issues: we support now Winboard format which isn't fine?
}
