package com.leokom.games.chess;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class BootstrapTest {
	private Game gameMock;


	@Before
	public void prepare() {
		gameMock = Mockito.mock( Game.class );
	}

	@Test
	public void gameIsRun() {
		new Bootstrap( gameMock ).run();
		
		Mockito.verify( gameMock ).run();
	}

	@Test
	public void runtimeExceptionDoesNotFailBootstrap() {
		Mockito.doThrow( new RuntimeException() ).when( gameMock ).run();

		new Bootstrap( gameMock ).run();

		Mockito.verify( gameMock ).run();
	}
	
	@Test
	public void errorDoesNotFailBootstrap() {
		Mockito.doThrow( new Error() ).when( gameMock ).run();

		new Bootstrap( gameMock ).run();

		Mockito.verify( gameMock ).run();
	}
}
