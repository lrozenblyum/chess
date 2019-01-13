package com.leokom.chess.engine;

import java.util.Set;

/**
 * The notion of game state is very generic and can be extracted to something chess-independent
 * @param <TransitionType> type of transitions
 * @param <THIS> current type
 */
/*
 Rather complex recursive generic to THIS class is introduced in order to support return of exactly
 our class in the move method.
 Inspired by https://www.sitepoint.com/self-types-with-javas-generics/
 */
public interface GameState< TransitionType extends GameTransition, THIS extends GameState< TransitionType, THIS > > {
    THIS move(TransitionType move);

    Set< TransitionType > getMoves();
}
