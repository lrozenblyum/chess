package com.leokom.games.chess.player;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.leokom.games.chess.engine.Move;

//it's a test for test infrastructure
public class PlayerBuilderTest {
	@Test
	public void initialPositionNotTerminal() {
		Player player = new PlayerBuilder(Mockito.mock( Player.class )).build();
		player.opponentSuggestsMeStartNewGameWhite();
		assertFalse( player.getPosition().isTerminal() );
	}

	//ensures proper position returning by the built player
	@Test
	public void positionAfterResignTerminal() {
		Player player = new PlayerBuilder(Mockito.mock( Player.class )).move( Move.RESIGN ).build();
		player.opponentSuggestsMeStartNewGameWhite();
		assertTrue( player.getPosition().isTerminal() );
	}
}
