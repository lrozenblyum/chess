package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import static org.junit.Assert.*;

public class PlayerFactoryTest {
	//snapshots all system properties before a test, restores after it
	@Rule
	public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void noSystemPropertiesDefaultPlayerBlack() {
		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertIsLegal( player );
	}

	@Test
	public void canSelectSimpleEngineForWhite() {
		System.setProperty( "whiteEngine", "Simple" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertIsSimple( player );
	}

	private void assertIsSimple(Player player) {
		assertEquals( "LegalPlayer : SimpleBrain", player.name() );
	}

	@Test
	public void canSelectWinboardForBlack() {
		System.setProperty( "blackEngine", "Winboard" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertTrue( player instanceof WinboardPlayer );
	}

	@Test
	public void noSystemPropertiesDefaultPlayerWhite() {
		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertTrue( player instanceof WinboardPlayer );
	}

	@Test
	public void legalSelected() {
		System.setProperty( "black", "Legal" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertIsLegal( player );
	}

	private void assertIsLegal( Player player ) {
		assertThat( player.name(), CoreMatchers.startsWith( "LegalPlayer" ) );
	}

	@Test
	public void legalSelectedWhite() {
		System.setProperty( "whiteEngine", "Legal" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertIsLegal( player );
	}

	@Test
	public void depth2FromCommandLineRespectedForWhite() {
		System.setProperty( "whiteEngine", "Legal" );
		System.setProperty( "whiteDepth", "2" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertDepth( player, 2 );
	}

	@Test
	public void depth1FromCommandLineRespectedForWhite() {
		System.setProperty( "whiteEngine", "Legal" );
		System.setProperty( "whiteDepth", "1" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertDepth( player, 1 );
	}

	@Test
	public void depth1FromCommandLineRespectedForBlack() {
		System.setProperty( "blackEngine", "Legal" );
		System.setProperty( "blackDepth", "1" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertDepth( player, 1 );
	}

	@Test
	public void depth2FromCommandLineRespectedForBlack() {
		System.setProperty( "blackEngine", "Legal" );
		System.setProperty( "blackDepth", "2" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertDepth( player, 2 );
	}

	@Test
	public void defaultDepthIs1() {
		System.setProperty( "blackEngine", "Legal" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertDepth( player, 1 );
	}

	/*
	More cases:
	- extend *.bat file(s)
	- extend docs
	 */

	private void assertDepth( Player player, int expectedDepth ) {
		//shallow yet good enough check
		assertThat( player.name(), CoreMatchers.containsString( String.valueOf( expectedDepth ) ) );
	}
}