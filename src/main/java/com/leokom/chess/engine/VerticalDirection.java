package com.leokom.chess.engine;

/**
 * Direction on the board, from White player view
 * Author: Leonid
 * Date-time: 29.06.13 16:20
 */
enum VerticalDirection {
	/**
	 * Direction to the top of the board (from rank 1 to rank 8)
	 */
	UP,

	/**
	 * Direction to the bottom of the board (from rank 8 to rank 1)
	 */
	DOWN;

	VerticalDirection opposite() {
		return this == UP ? DOWN : UP;
	}
}
