package com.leokom.games.chess;

import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.player.Player;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Leonid on 21.11.16.
 *
 */
public class GameTest {
    @Test
    public void gameCanBeFinishedWithoutStart() {
        Player white = mock( Player.class );
        setPosition(white);

        Player black = mock( Player.class );
        GameResult gameResult = new Game( white, black ).runGame();
        //no exceptions expected
        assertEquals(GameResult.UNFINISHED_GAME, gameResult);
    }

    @Test
    public void verifyOpponentSetting() {
        Player white = mock( Player.class );
        setPosition( white );
        Player black = mock( Player.class );

        new Game( white, black ).runGame();

        verify( white ).setOpponent( black );
        verify( black ).setOpponent( white );
    }

    private void setPosition(Player white) {
        when( white.getPosition() ).thenReturn( Position.getInitialPosition() );
    }
}