package com.leokom.chess.engine;

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
	RIGHT
}
