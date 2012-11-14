package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Communicator;

/**
 * Author: Leonid
 * Date-time: 14.11.12 22:35
 */
public class MockCommunicatorReceiveCreator {
	static Communicator getReceiveCommunicator( final String stringToReceive ) {
		return new Communicator() {
			@Override
			public void send( String command ) {
			}

			@Override
			public String receive() {
				return stringToReceive;
			}
		};
	}
}
