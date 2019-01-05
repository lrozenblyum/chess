package com.leokom.chess.engine;

import com.leokom.chess.utils.CollectionUtils;

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

	//thread safety for ready-only purposes looks fine
	//http://stackoverflow.com/questions/26409869/is-a-readonly-enumset-iterator-thread-safe
	private static final Set< Direction > CACHE = CollectionUtils.enums( Direction.class );

	/**
	 *
	 * @return cache of all values
	 */
	/*
	 * values() method creates clone() of array
	 * which is slow when executed thousands times
	 * (unexpectedly) proved by profiler
	 */
	static Set< Direction > all() {
		return CACHE;
	}
}
