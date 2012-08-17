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

            if ( line.equals( "xboard" ) ) {
                logger.info( "Ready to work" );

                //this still has no effect
                System.out.println( "usermove e2e4" );
            }
		}
	}
}