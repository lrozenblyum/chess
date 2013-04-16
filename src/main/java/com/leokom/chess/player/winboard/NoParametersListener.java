package com.leokom.chess.player.winboard;

/**
 * Ensure no-parameter winboard listeners share this common interface
 * to simplify dispatching (inspired by Sonar's complexity measure of the dispatcher)
 */
interface NoParametersListener {
	void execute();
}