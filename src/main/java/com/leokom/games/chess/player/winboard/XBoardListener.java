package com.leokom.games.chess.player.winboard;

/**
 * This command will be sent once immediately after your engine process is started.
 * You can use it to put your engine into "xboard mode" if that is needed.
 * If your engine prints a prompt to ask for user input, you must turn off the prompt and output a newline when the "xboard" command comes in.
 */
interface XBoardListener extends NoParametersListener {
	@Override
	void execute();
}
