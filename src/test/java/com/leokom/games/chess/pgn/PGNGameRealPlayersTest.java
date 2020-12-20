package com.leokom.games.chess.pgn;

import com.leokom.games.chess.Game;
import com.leokom.games.chess.player.legal.LegalPlayer;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.games.chess.player.legal.brain.normalized.NormalizedBrain;
import com.leokom.games.chess.player.legal.brain.simple.SimpleBrain;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class PGNGameRealPlayersTest {
    @Test
    public void brainNames() {
        PGNGame pgnGame = new PGNGame(new Event("", "", LocalDate.now()), new Game(
                new LegalPlayer(new NormalizedBrain(new MasterEvaluator(), 2)),
                new LegalPlayer(new SimpleBrain()))
        );

        String output = pgnGame.run();
        assertEquals( "[White \"LegalPlayer: NormalizedBrain: 2 depth\"]", output.split( "\n" )[ 4 ] );
        assertEquals( "[Black \"LegalPlayer: SimpleBrain\"]", output.split( "\n" )[ 5 ] );
    }

}
