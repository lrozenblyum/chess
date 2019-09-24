package com.leokom.games.chess.player.legal;

import com.leokom.games.chess.player.legal.brain.denormalized.DenormalizedBrain;
import com.leokom.games.chess.player.legal.brain.normalized.MasterEvaluator;
import com.leokom.games.chess.player.legal.brain.normalized.NormalizedBrain;
import com.leokom.games.chess.player.legal.brain.simple.SimpleBrain;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LegalPlayerNameTest {
    @Test
    public void denormalizedBrain() {
        assertEquals( "LegalPlayer : DenormalizedBrain", new LegalPlayer( new DenormalizedBrain() ).name() );
    }

    @Test
    public void normalizedBrain() {
        assertEquals( "LegalPlayer : NormalizedBrain: 1 depth", new LegalPlayer( new NormalizedBrain( new MasterEvaluator()) ).name() );
    }

    @Test
    public void simpleBrain() {
        assertEquals( "LegalPlayer : SimpleBrain", new LegalPlayer( new SimpleBrain() ).name() );
    }
}
