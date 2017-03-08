package com.leokom.chess.player.legal;

import org.junit.Test;

import static com.leokom.chess.PlayerFactory.PlayerSelection.LEGAL;
import static com.leokom.chess.PlayerFactory.PlayerSelection.SIMPLE;
import static org.junit.Assert.assertEquals;

public class LegalPlayerNameTest {
    @Test
    public void defaultName() {
        assertEquals( "LegalPlayer : DenormalizedBrain", LEGAL.create().name() );
    }

    @Test
    public void customNameWithCustomBrain() {
        assertEquals( "LegalPlayer : SimpleBrain", SIMPLE.create().name() );
    }
}
