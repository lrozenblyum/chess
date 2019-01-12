package com.leokom.chess.engine;

import java.util.Set;

/**
 * The notion of game state is very generic and can be extracted to something chess-independent
 * @param <TransitionType> type of transitions
 */
public interface GameState< TransitionType extends GameTransition > {
    Set< TransitionType > getMoves();
}
