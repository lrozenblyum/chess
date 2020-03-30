package com.leokom.games.chess;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

public class PGNTest {
    @Test
    public void pgnInception() {
        String pgn = new PGNGame().run();
        assertThat( pgn, CoreMatchers.startsWith( "[Event (?)]" ));
    }
}
