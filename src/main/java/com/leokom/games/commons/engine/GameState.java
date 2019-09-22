package com.leokom.games.commons.engine;

import java.util.Set;

/**
 * The generic notion of game state.
 * @param <T> type of transitions
 * @param <S> current type (state)
 */
/*
 Rather complex recursive generic to S class is introduced in order to support return of exactly
 our class in the move method.
 Inspired by https://www.sitepoint.com/self-types-with-javas-generics/
 */
public interface GameState< T extends GameTransition, S extends GameState<T, S> > {
    S move(T move);

    Set<T> getMoves();
}
