/**
 * The engine so far is considered to be
 * an implementation of all chess rules
 * to have possibility detect correct moves,
 * execute them and generate new positions,
 * have knowledge about winning and draw situations.
 *
 * This engine might be universal
 * or may be further tweaked (e.g. from performance POV)
 *
 * It could be used by Player implementations
 * and probably by an intermediate component
 * between 2 Player instances which does NOT exist
 * so far, but I think it should be there
 * (e.g. at least for validating if Player did a valid move,
 * WinBoard cannot detect if move is valid or not
 * in all circumstances)
 */
package com.leokom.games.chess.engine;