package com.leokom.chess.player.legal.brain.random;

import org.junit.Test;

import static org.junit.Assert.*;

public class RandomBrainTest {
    @Test
    public void brainExistence() {
        assertEquals( "RandomBrain", new RandomBrain().name() );
    }
}