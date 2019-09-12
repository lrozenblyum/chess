package com.leokom.games.chess.engine;

import com.leokom.games.chess.utils.CollectionUtils;

import java.util.Set;

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

	private static final Set< VerticalDirection > CACHE =
			CollectionUtils.enums( VerticalDirection.class );

	VerticalDirection opposite() {
		return this == UP ? DOWN : UP;
	}

	static Set< VerticalDirection > all() {
		return CACHE;
	}
}
