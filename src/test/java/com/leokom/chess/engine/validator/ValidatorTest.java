package com.leokom.chess.engine.validator;

import com.leokom.chess.engine.Move;
import com.leokom.chess.player.Player;
import org.junit.Before;
import org.junit.Test;

import static com.leokom.chess.player.PlayerTestUtils.tellOpponentAboutMove;
import static org.mockito.Mockito.*;

/**
 * Author: Leonid
 * Date-time: 16.11.14 18:09
 */
public class ValidatorTest {
	private Player white;
	private Player black;
	private Validator validator;

	@Before
	public void prepare() {
		white = mock( Player.class );
		black = mock( Player.class );
		validator = new Validator( white, black );

		//injecting man-in-the-middle
		white.setOpponent( validator );
		black.setOpponent( validator );
	}

	//basic man-in-the middle requirement : pass through if it's valid
	@Test
	public void shouldPassMoveToOpponentGivenItsValid() {
		tellOpponentAboutMove( validator, new Move( "e2", "e4" ) )
		.when( white ).opponentSuggestsMeStartNewGameWhite();

		white.opponentSuggestsMeStartNewGameWhite();

		verify( black ).opponentMoved( new Move( "e2", "e4" ) );
	}

	@Test
	public void shouldReturnBlackAnswerToWhite() {
		tellOpponentAboutMove( validator, new Move( "e2", "e4" ) )
		.when( white ).opponentSuggestsMeStartNewGameWhite();

		tellOpponentAboutMove( validator, new Move( "e7", "e5" ) )
		.when( black ).opponentMoved( new Move( "e2", "e4" ) );

		white.opponentSuggestsMeStartNewGameWhite();

		verify( white ).opponentMoved( new Move( "e7", "e5" ) );
	}
}
