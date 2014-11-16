package com.leokom.chess.engine.validator;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 16.11.14 18:09
 */
public class ValidatorTest {
	//basic man-in-the middle requirement : pass through if it's valid
	@Test
	public void shouldPassMoveToOpponentGivenItsValid() {
		Player white = mock( Player.class );
		Player black = mock( Player.class );

		Validator validator = new Validator();

		//injecting man-in-the-middle
		white.setOpponent( validator );
		black.setOpponent( validator );

		doAnswer( (invocation) -> {
			validator.opponentMoved( new Move( "e2", "e4" ) ); return null;
		} ).when( white ).opponentSuggestsMeStartNewGameWhite();

		white.opponentSuggestsMeStartNewGameWhite();

		verify( black ).opponentMoved( new Move( "e2", "e4" ) );
	}
}
