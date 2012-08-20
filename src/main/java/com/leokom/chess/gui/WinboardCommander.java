package com.leokom.chess.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Author: Leonid
 * Date-time: 20.08.12 16:13
 */
//TODO: hide it and create some factory ?
public class WinboardCommander implements Commander {
    private BufferedReader reader;

    public WinboardCommander() {
        //TODO: think about buffers, they're not recommended to use
        this.reader = new BufferedReader(new InputStreamReader( System.in ));
    }

    @Override
    public void send(String command) {
        System.out.println( command );
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
