package com.leokom.chess.player;

import com.leokom.chess.engine.Move;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.doAnswer;

/**
 * Author: Leonid
 * Date-time: 16.11.14 21:03
 */
public class PlayerTestUtils {
	public static Stubber tellOpponentAboutMove( Player opponent, Move moveToDo ) {
		return doAnswer( invocationOnMock -> {
			opponent.opponentMoved( moveToDo );
			return null;
		} );
	}
}
