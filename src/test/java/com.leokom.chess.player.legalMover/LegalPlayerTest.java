package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.PieceType;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


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

		//TODO: what's the format of the move we support currently?

		//TODO: it's overhead but in current architecture
		//the only way to inform another player about a move
		//is as a reaction to 'opponentMoved'


		player.opponentMoved( "c2-c1" );
		//leaving for whites only single move:
		//a1-a2

		verify( opponent ).opponentMoved( "a1-a2" );
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

		//TODO: what's the format of the move we support currently?
		//TODO: overhead (see another test for description)
		player.opponentMoved( "g5-g6" );
		//leaving for whites only single move:

		verify( opponent ).opponentMoved( "h8-g8" );
	}
}
