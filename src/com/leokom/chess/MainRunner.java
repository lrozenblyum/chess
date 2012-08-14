package com.leokom.chess;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class MainRunner {
	private static Logger logger = Logger.getLogger(MainRunner.class);
	public static void main(String[] args) throws IOException {
		logger.info("Hello");
		System.out.println( "usermove e2e4" );
		
		//TODO: think about buffers
		BufferedReader r = new BufferedReader(new InputStreamReader( System.in ));


		while( true ) {
			String line = r.readLine();
			System.out.println( "Line from engine = " + line );
		}
	}
}
