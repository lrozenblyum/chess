package com.leokom.chess;


import com.leokom.chess.player.Player;
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

		//the player must be just a thin client over Winboard
		final Player winboardPlayer = WinboardFactory.getPlayer();

		//this is the real 'brains'
		final EnginePlayer enginePlayer = new EnginePlayer();
		enginePlayer.setOpponent( winboardPlayer );
		winboardPlayer.setOpponent( enginePlayer );
		//it's main loop - which definitely looks out of
		//symmetry and players equality
		winboardPlayer.run();

		logger.info( "Chess are stopped. Bye-bye" );
	}

	private static class EnginePlayer implements Player {
		//TODO: this moveNumber is totally unreliable (after end-of-game it must be reset)
		private int moveNumber;
		private Player opponent;

		public EnginePlayer() {
			moveNumber = 0;
		}

		//TODO: asymmetric setter to have possibility one player to another
		@Override
		public void setOpponent( Player opponent ) {
			this.opponent = opponent;
		}

		@Override
		public void opponentMoved( String opponentMove ) {
			moveNumber++;
			logger.info( "Detected allowance to go. Move number = " + moveNumber );
			switch ( moveNumber ) {
				case 1:
					opponent.opponentMoved( "e2e4" );
					break;
				case 2:
					opponent.opponentMoved( "d2d4" );
					//NOTE: interesting to implement - how much do we need to wait for result?
					//NOTE2: it's not recommended way to offer draw after the move.
					opponent.opponentOfferedDraw();
					break;
				default:
					opponent.opponentResigned();
			}
		}

		@Override
		public void run() {
			throw new UnsupportedOperationException( "Definitely it's a sign 'run' must be removed from this interface" );
		}

		@Override
		public void opponentAgreedToDrawOffer() {
			//To change body of implemented methods use File | Settings | File Templates.
		}

		/**
		 * Simplest possible strategy - agree to the draw offer
		 */
		@Override
		public void opponentOfferedDraw() {
			opponent.opponentAgreedToDrawOffer();
		}

		@Override
		public void opponentResigned() {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}
}

