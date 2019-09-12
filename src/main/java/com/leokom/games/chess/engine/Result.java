package com.leokom.games.chess.engine;

/**
 * Game result.
 *
 * Technically all possible game results must be here
 *
 * Author: Leonid
 * Date-time: 25.07.16 21:43
 */
/*
 * Due to technical complexities, some game results aren't here yet
 */
public enum Result {
	STALEMATE,
	DRAW_BY_CLAIM,
	DRAW_BY_AGREEMENT,
	DRAW_BY_OBLIGATORY_MOVES,
	RESIGN
}
