package com.leokom.chess.player.legal.brain.common;

/**
 * Represent known way to analyze a position.
 * Every instance of it is some part of our brain we'd like to use to analyze a particular
 * position move.
 * Author: Leonid
 * Date-time: 19.04.16 22:46
 */
public enum EvaluatorType {
	TERMINAL,
	CASTLING_SAFETY,
	CENTER_CONTROL,
	MATERIAL,
	MOBILITY,
	PROTECTION,
	SPECIAL_MOVE,
	ATTACK
}
