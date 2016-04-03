package com.leokom.chess.engine;

import java.util.EnumSet;

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

	//TODO : use some immutable representation
	private static final EnumSet< HorizontalDirection > cache = EnumSet.allOf( HorizontalDirection.class );

	static EnumSet< HorizontalDirection > values2() {
		return cache;
	}

}
