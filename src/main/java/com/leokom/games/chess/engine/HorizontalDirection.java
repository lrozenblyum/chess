package com.leokom.games.chess.engine;

import com.leokom.games.chess.utils.CollectionUtils;

import java.util.Set;

/**
 * Created as wish to reduce duplication
 * We do similar actions to determine pawn
 * movement possibilities to left and right
 * The direction is considered from White player point of view
 * Date-time: 19.05.13 19:37
 */
enum HorizontalDirection {
	/**
	 * Direction from h to a file
	 */
	LEFT,

	/**
	 * Direction from a to h file
	 */
	RIGHT;

	//to make sure it's indeed immutable all enum values must be immutable
	private static final Set< HorizontalDirection > CACHE
			= CollectionUtils.enums( HorizontalDirection.class );

	static Set< HorizontalDirection > all() {
		return CACHE;
	}
}