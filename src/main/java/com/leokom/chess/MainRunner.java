package com.leokom.chess;


import com.leokom.chess.player.DrawOfferedListener;
import com.leokom.chess.player.Player;
import com.leokom.chess.player.NeedToGoListener;
import com.leokom.chess.gui.winboard.WinboardFactory;
import org.apache.log4j.Logger;

/**
 * Entry point to the Chess application.
 */
public final class MainRunner {
	//prohibit instantiation
	private MainRunner() {
	}

	private static final Logger logger = Logger.getLogger( MainRunner.class );

	public static void main( String[] args ) {
		logger.info( "Starting the chess..." );

		//the player is technically the brains of our Chess software
		final Player player = WinboardFactory.getPlayer();

		final NeedToGoListener onMoveNeedToGoListener = new MoveListener( player );

		player.onMoved( onMoveNeedToGoListener );
		player.onDrawOffered( new DrawOfferedListener() {
			@Override
			public void anotherPlayerOfferedDraw() {
				player.anotherPlayerAgreedToDrawOffer();
			}
		} );
		//it's main loop
		player.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}

	/**
	 * Basic implementation of 'on move allowed'
	 */
	private static class MoveListener implements NeedToGoListener {
		//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
		private int moveNumber;
		private final Player anotherPlayer;

		public MoveListener( Player anotherPlayer ) {
			this.anotherPlayer = anotherPlayer;
			moveNumber = 0;
		}

		@Override
		public void anotherPlayerMoved( String anotherPlayerMove ) {
			moveNumber++;
			logger.info( "Detected allowance to go. Move number = " + moveNumber );
			switch ( moveNumber ) {
				case 1:
					anotherPlayer.anotherPlayerMoved( "e2e4" );
					break;
				case 2:
					anotherPlayer.anotherPlayerMoved( "d2d4" );
					//NOTE: interesting to implement - how much do we need to wait for result?
					//NOTE2: it's not recommended way to offer draw after the move.
					anotherPlayer.anotherPlayerOfferedDraw();
					break;
				default:
					anotherPlayer.anotherPlayerResigned();
			}
		}
	}
}

