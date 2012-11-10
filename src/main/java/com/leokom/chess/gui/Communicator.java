package com.leokom.chess.gui;

/**
 * Encapsulate possibility to send and receive commands
 * to some external 'server' (inspired by wish to make it test-coverable
 * sending and receiving commands to the Winboard interface)
 *
 * Author: Leonid
 * Date-time: 20.08.12 16:09
 */
public interface Communicator {
    /**
     * Send a command to the server
     * @param command command to be sent
     */
    void send( String command );

    /**
     * Receive a command from the server
     * @return the received command (or null if server doesn't want to send anything)
     */
    String receive();

    //TODO: add any polling command to check if next receive will return not-null result?
}
