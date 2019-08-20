package com.leokom.chess.player.winboard;

/**
 * Listen to 'protover' event occurred
 *
 * protover N
 * Beginning in protocol version 2 (in which N=2), this command will be sent immediately after the "xboard" command.
 * If you receive some other command immediately after "xboard" (such as "new"), you can assume that protocol version 1 is in use.
 * The "protover" command is the only new command that xboard always sends in version 2. All other new commands to the engine are sent only if the engine first enables them with the "feature" command. Protocol versions will always be simple integers so that they can easily be compared.
 * Your engine should reply to the protover command by sending the "feature" command (see below) with the list of non-default feature settings that you require, if any.
 * Your engine should never refuse to run due to receiving a higher protocol version number than it is expecting! New protocol versions will always be compatible with older ones by default; the larger version number is simply a hint that additional "feature" command options added in later protocol versions may be accepted.
 *
 * Author: Leonid
 * Date-time: 13.11.12 21:37
 */
interface ProtoverListener extends IntParameterListener {
	@Override
	void execute( int protocolVersion );
}
