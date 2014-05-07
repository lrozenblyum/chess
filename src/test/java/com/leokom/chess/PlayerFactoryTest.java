package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.legalMover.LegalPlayer;
import com.leokom.chess.player.simple.SimpleEnginePlayer;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerFactoryTest {
	//ensure one test has no influence on another
	@Before
	@After
	public void clearSystemProperties() {
		System.clearProperty( "black" );
	}

	@Test
	public void noSystemPropertiesDefaultPlayer() {
		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertTrue( player instanceof SimpleEnginePlayer );
	}

	@Test
	public void canSelectSimpleEngineForWhite() {
		System.setProperty( "white", "SimpleEnginePlayer" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertTrue( player instanceof SimpleEnginePlayer );
	}

	@Test
	public void noSystemPropertiesDefaultPlayerWhite() {
		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertTrue( player instanceof WinboardPlayer );
	}

	@Test
	public void legalSelected() {
		System.setProperty( "black", "LegalPlayer" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertTrue( player instanceof LegalPlayer );
	}

	@Test
	public void legalSelectedWhite() {
		System.setProperty( "white", "LegalPlayer" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertTrue( player instanceof LegalPlayer );
	}
}