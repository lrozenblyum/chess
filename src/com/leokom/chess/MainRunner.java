package com.leokom.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class MainRunner {
	private static Logger logger = Logger.getLogger(MainRunner.class);
	public static void main(String[] args) throws IOException {
		logger.info( "Starting the chess" );

		//TODO: think about buffers, they're not recommended to use
		BufferedReader r = new BufferedReader(new InputStreamReader( System.in ));

		while( true ) {
            //TODO: any Thread.sleep needed?
			String line = r.readLine();
			logger.info( "Line from engine = " + line );

            //TODO: what does it mean?
            if ( line == null ) {
                continue;
            }

            if ( line.equals( "quit" ) ) {
                logger.info( "Received quit command" );
                break;
            }

            // xboard
            // This command will be sent once immediately after your engine process is started.
            // You can use it to put your engine into "xboard mode" if that is needed.
            // If your engine prints a prompt to ask for user input,
            // you must turn off the prompt and output a newline when the "xboard" command comes in.

            //LR: because we don't print any prompt, I don't put any newline here
            if ( line.equals( "xboard" ) ) {
                logger.info( "Ready to work" );

                //this still has no effect
                System.out.println( "usermove e2e4" );
            }
		}
	}
}