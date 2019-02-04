package com.leokom.chess;

import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.winboard.WinboardPlayer;
import org.hamcrest.CoreMatchers;
import org.junit.*;

import static org.junit.Assert.*;

public class PlayerFactoryTest {
	private static String whiteProperty;
	private static String blackProperty;
	private static String whiteDepthProperty;
	private static String blackDepthProperty;

	@BeforeClass
	public static void preserveSystemProperties() {
		whiteProperty = System.getProperty( "white" );
		blackProperty = System.getProperty( "black" );
		whiteDepthProperty = System.getProperty( "whiteDepthProperty" );
		blackDepthProperty = System.getProperty( "blackDepthProperty" );
	}

	@AfterClass
	public static void restoreSystemProperties() {
		//if any of them is null, @After method already cleared it.
		//setting null value of system property causes NPE
		if ( whiteProperty != null ) {
			System.setProperty( "white", whiteProperty );
		}

		if ( blackProperty != null ) {
			System.setProperty( "black", blackProperty );
		}

		if ( whiteDepthProperty != null ) {
			System.setProperty( "whiteDepthProperty", whiteDepthProperty );
		}
		if ( blackDepthProperty != null ) {
			System.setProperty( "blackDepthProperty", blackDepthProperty );
		}
	}

	//ensure one test has no influence on another
	@Before
	@After
	public void clearSystemProperties() {
		System.clearProperty( "black" );
		System.clearProperty( "white" );
	}

	@Test
	public void noSystemPropertiesDefaultPlayerBlack() {
		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertIsLegal( player );
	}

	@Test
	public void canSelectSimpleEngineForWhite() {
		System.setProperty( "white", "Simple" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertIsSimple( player );
	}

	private void assertIsSimple(Player player) {
		assertEquals( "LegalPlayer : SimpleBrain", player.name() );
	}

	@Test
	public void canSelectWinboardForBlack() {
		System.setProperty( "black", "Winboard" );

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
		System.setProperty( "white", "Legal" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertIsLegal( player );
	}

	@Test
	public void depth2FromCommandLineRespectedForWhite() {
		System.setProperty( "white", "Legal" );
		System.setProperty( "whiteDepth", "2" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertDepth( player, 2 );
	}

	@Test
	public void depth1FromCommandLineRespectedForWhite() {
		System.setProperty( "white", "Legal" );
		System.setProperty( "whiteDepth", "1" );

		final Player player = PlayerFactory.createPlayer( Side.WHITE );
		assertDepth( player, 1 );
	}

	@Test
	public void depth1FromCommandLineRespectedForBlack() {
		System.setProperty( "black", "Legal" );
		System.setProperty( "blackDepth", "1" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertDepth( player, 1 );
	}

	@Test
	public void depth2FromCommandLineRespectedForBlack() {
		System.setProperty( "black", "Legal" );
		System.setProperty( "blackDepth", "2" );

		final Player player = PlayerFactory.createPlayer( Side.BLACK );
		assertDepth( player, 2 );
	}

	/*
	More cases:
	- default value
	- Refactor: automate system properties manipulation
	 */

	private void assertDepth( Player player, int expectedDepth ) {
		//shallow yet good enough check
		assertThat( player.name(), CoreMatchers.containsString( String.valueOf( expectedDepth ) ) );
	}
}