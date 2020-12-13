package com.leokom.games.chess;

import org.junit.Test;

import static org.junit.Assert.*;

public class CIVerifyTest {
    @Test
    public void checkSth() {
        assertEquals( 0, new CIVerify().result(1) );
    }
}