package com.leokom.games.chess.player.legal.brain.normalized;

import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.commons.brain.GenericEvaluator;
import com.leokom.games.commons.brain.normalized.TwoPliesEvaluator;
import javafx.geometry.Pos;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

//normalized-specific
@RunWith( Parameterized.class )
public class ProtectionEvaluatorTest {
    @Parameterized.Parameter
    public GenericEvaluator<Position, Move> evaluator;

    @Parameterized.Parameters( name = "{0}" )
    public static List< GenericEvaluator<Position, Move> > parameters() {
        return Arrays.asList(
                new ProtectionEvaluator(),
                new TwoPliesEvaluator<>(new ProtectionEvaluator(), NormalizedChessBrain::new)
        );
    }

    @Test
    public void allMovesShouldBeInNormalizedRange() {
        Position position = Position.getInitialPosition().move( "e2", "e4" );

        new NormalizedEvaluatorAssert( evaluator ).assertAllMovesEvaluatedInNormalizedRange( position );
    }
}
