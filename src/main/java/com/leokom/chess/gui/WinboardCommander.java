package com.leokom.chess.gui;

import java.io.*;

/**
 * Author: Leonid
 * Date-time: 20.08.12 16:13
 */
//TODO: hide it and create some factory ?
public class WinboardCommander implements Commander {
    private BufferedReader reader;
    private PrintStream outputStream;

    /**
     * Create the winboard-commander with injected dependencies
     * @param inputStream
     * @param outputStream
     */
    public WinboardCommander( InputStream inputStream, PrintStream outputStream ) {
        //TODO: think about buffers, they're not recommended to use
        this.reader = new BufferedReader( new InputStreamReader( inputStream ) );
        this.outputStream = outputStream;
    }

    @Override
    public void send(String command) {
        outputStream.println( command );
    }

    @Override
    public String receive() {
        try {
            return reader.readLine();
        } catch ( IOException e ) { //avoid propagating internal exception to signature
            throw new RuntimeException( e );
        }
    }
}
