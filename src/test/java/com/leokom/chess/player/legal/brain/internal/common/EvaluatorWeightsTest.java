package com.leokom.chess.player.legal.brain.internal.common;

import com.leokom.chess.player.legal.brain.common.EvaluatorType;
import org.junit.Test;
import org.mutabilitydetector.unittesting.MutabilityAssert;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class EvaluatorWeightsTest {
    @Test
    public void weightsAreImmutable() {
        MutabilityAssert.assertImmutable( EvaluatorWeights.class );
    }

    @Test( expected = IllegalArgumentException.class )
    public void evaluatorWeightBigger1NotAccepted() {
        Map<EvaluatorType, Double> weights = new HashMap<>();
        weights.put( EvaluatorType.PROTECTION, 1.1 );
        new EvaluatorWeights( weights );
    }


    @Test( expected = IllegalArgumentException.class )
    public void evaluatorWeightLess0NotAccepted() {
        Map<EvaluatorType, Double> weights = new HashMap<>();
        weights.put( EvaluatorType.PROTECTION, -0.1 );
        new EvaluatorWeights( weights );
    }
}