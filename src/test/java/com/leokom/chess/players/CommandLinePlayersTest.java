package com.leokom.chess.players;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.hamcrest.CoreMatchers;
import org.junit.*;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import static org.junit.Assert.*;

public class CommandLinePlayersTest {
	//snapshots all system properties before a test, restores after it
	@Rule
	public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

	@Test
	public void noSystemPropertiesDefaultPlayerBlack() {
		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertIsLegal( player );
	}

	@Test
	public void canSelectSimpleEngineForWhite() {
		System.setProperty( "white.engine", "Simple" );

		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertIsSimple( player );
	}

	@Test
	public void canSelectDenormalizedBrainForWhite() {
		System.setProperty( "white.engine", "brain.denormalized" );

		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertThat( player.name(), CoreMatchers.containsString( "Denormalized" ) );
	}

	@Test( expected = IllegalArgumentException.class )
	public void failFastOnUnsupportedEngine() {
		System.setProperty( "white.engine", "Unsupported" );

		new CommandLinePlayers().apply( Side.WHITE );
	}

	private void assertIsSimple(Player player) {
		assertEquals( "LegalPlayer : SimpleBrain", player.name() );
	}

	@Test
	public void canSelectWinboardForBlack() {
		System.setProperty( "black.engine", "Winboard" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertTrue( player instanceof WinboardPlayer );
	}

	@Test
	public void noSystemPropertiesDefaultPlayerWhite() {
		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertTrue( player instanceof WinboardPlayer );
	}

	@Test
	public void legalSelected() {
		System.setProperty( "black", "Legal" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertIsLegal( player );
	}

	private void assertIsLegal( Player player ) {
		assertThat( player.name(), CoreMatchers.startsWith( "LegalPlayer" ) );
	}

	@Test
	public void legalSelectedWhite() {
		System.setProperty( "white.engine", "Legal" );

		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertIsLegal( player );
	}

	@Test
	public void depth2FromCommandLineRespectedForWhite() {
		System.setProperty( "white.engine", "Legal" );
		System.setProperty( "white.depth", "2" );

		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertDepth( player, 2 );
	}

	@Test
	public void depth1FromCommandLineRespectedForWhite() {
		System.setProperty( "white.engine", "Legal" );
		System.setProperty( "white.depth", "1" );

		final Player player = new CommandLinePlayers().apply( Side.WHITE );
		assertDepth( player, 1 );
	}

	@Test
	public void depth1FromCommandLineRespectedForBlack() {
		System.setProperty( "black.engine", "Legal" );
		System.setProperty( "black.depth", "1" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertDepth( player, 1 );
	}

	@Test
	public void depth2FromCommandLineRespectedForBlack() {
		System.setProperty( "black.engine", "Legal" );
		System.setProperty( "black.depth", "2" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertDepth( player, 2 );
	}

	@Test
	public void legalPlayerDepthCanBeProvidedEvenIfEngineIsNotProvided() {
		//because legal is default one
		System.setProperty( "black.depth", "2" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertDepth( player, 2 );
	}

	@Test
	public void defaultDepthIs1() {
		System.setProperty( "black.engine", "Legal" );

		final Player player = new CommandLinePlayers().apply( Side.BLACK );
		assertDepth( player, 1 );
	}

	private void assertDepth( Player player, int expectedDepth ) {
		//shallow yet good enough check
		assertThat( player.name(), CoreMatchers.containsString( String.valueOf( expectedDepth ) ) );
	}
}