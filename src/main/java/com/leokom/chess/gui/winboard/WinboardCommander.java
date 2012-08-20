package com.leokom.chess.gui.winboard;

import com.leokom.chess.gui.Commander;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Author: Leonid
 * Date-time: 20.08.12 16:13
 */
class WinboardCommander implements Commander {
    private BufferedReader reader;
    private PrintStream outputStream;
	private Logger logger = Logger.getLogger( this.getClass() );

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
		logger.info( "Sent: " + command );
        outputStream.println( command );
    }

    @Override
    public String receive() {
        try {
			final String line = reader.readLine();
			logger.info( "Received: " + line );
			return line;
        } catch ( IOException e ) { //avoid propagating internal exception to signature
            throw new RuntimeException( e );
        }
    }
}
