package com.leokom.chess.engine;

import java.util.EnumSet;
import java.util.Set;

/**
 * Sometimes we need vision of all 4 directions in the same algorithm
 * Author: Leonid
 * Date-time: 03.07.13 22:45
 */
enum Direction {
	UP,
	DOWN,
	LEFT,
	RIGHT;

	private static final EnumSet< Direction > cache = EnumSet.allOf( Direction.class );

	static Set< Direction > values2() {
		return cache;
	}
}
