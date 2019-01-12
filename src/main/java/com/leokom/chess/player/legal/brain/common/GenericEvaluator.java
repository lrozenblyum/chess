package com.leokom.chess.player.legal.brain.common;

import com.leokom.chess.engine.GameState;
import com.leokom.chess.engine.GameTransition;

public interface GenericEvaluator< StateType extends GameState< TransitionType >, TransitionType extends GameTransition > {
    double evaluateMove(StateType position, TransitionType move );
}
