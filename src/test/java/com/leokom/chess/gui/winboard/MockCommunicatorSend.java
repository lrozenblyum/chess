package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

import java.util.ArrayList;
import java.util.List;

/**
* Author: Leonid
* Date-time: 10.11.12 21:25
*/
class MockCommunicatorSend implements Communicator {
	private List<String> SENT_COMMANDS = new ArrayList<String>();

	@Override
	public void send( String command ) {
		SENT_COMMANDS.add( command );
	}

	@Override
	public String receive() {
		return null;
	}

	public List<String> getSentCommands() {
		return SENT_COMMANDS;
	}
}
