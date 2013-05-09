package com.leokom.chess.engine;

/**
 * Define all possible piece types for chess
 * Author: Leonid
 * Date-time: 05.09.12 21:36
 */
public enum PieceType {
	/**
	 * The piece that initially looks weak but
	 * in team with other pawns and pieces could be very powerful
	 * It also could be promoted to other pieces except King
	 */
	PAWN,

	/**
	 * The piece that is considered to be the strongest in most cases.
	 * However the game may continue without it (in contrary to game without king)
	 */
	QUEEN
}
