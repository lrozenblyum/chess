package com.leokom.chess.player.winboard;

/**
 * Author: Leonid
 * Date-time: 15.03.15 21:54
 *
 * result RESULT {COMMENT}
 * After the end of each game, xboard will send you a result command. You can use this command to
 * trigger learning. RESULT is either 1-0, 0-1, 1/2-1/2, or *, indicating whether white won, black won,
 * the game was a draw, or the game was unfinished. The COMMENT string is purely a human-readable
 * comment; its content is unspecified and subject to change. In ICS mode, it is passed through from ICS
 * uninterpreted. Example:
 * result 1-0 {White mates}
 */
public interface GameOverListener extends StringParameterListener {
	void execute( String data );
}
