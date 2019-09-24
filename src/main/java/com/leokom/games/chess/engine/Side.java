package com.leokom.games.chess.engine;

/**
 * Author: Leonid
 * Date-time: 21.08.12 15:58
 */
public enum Side {
	BLACK, WHITE;

	/**
	 * @return the opposite side to the current
	 */
	public Side opposite() {
		return this == WHITE ? BLACK : WHITE;
	}
}
