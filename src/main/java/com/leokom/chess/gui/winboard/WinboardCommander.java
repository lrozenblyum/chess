package com.leokom.chess.gui.winboard;

/**
 * Author: Leonid
 * Date-time: 10.11.12 21:54
 */
public interface WinboardCommander {
	void startInit();

	/**
	 * TODO: think if it's must be the interface part
	 * or part of implementation details.
	 * As it has influence how the result will be parsed...
	 */
	void enableUserMovePrefixes();
}
