package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * Author: Leonid
 * Date-time: 09.12.13 22:02
 */
public class LegalPlayer implements Player {
	private final Side side;
	private Player opponent;
	private Position position = Position.getInitialPosition();

	/**
	 * Create player that will play for the side
	 * @param side who we play for?
	 */
	public LegalPlayer( Side side ) {
		this.side = side;
	}

	@Override
	public void run() {

	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentMoved( String opponentMove ) {
		//TODO: null is a hidden ugly way to say 'it's our first move now'
		if ( opponentMove != null ) {
			String source = opponentMove.substring( 0, 2 );
			String destination = opponentMove.substring( 2 );

			//updating internal representation of our position according to the opponent's move
			position = position.move( source, destination );
		}

		Set< String[] > moves = position.getMoves( side );

		if ( !moves.isEmpty() ) {
			String[] possibleMove = moves.iterator().next();

			final String from = possibleMove[ 0 ];
			final String to = possibleMove[ 1 ];

			//updating internal representation of current position according to our move
			position = position.move( from, to );
			getLogger().info( this.side + " : Moved " + from + " : " + to );

			opponent.opponentMoved( from + to );
		}
		else {
			getLogger().info( "Final state detected" );
			//TODO: if empty set it means the game has been finished (what's the result?)
		}
	}

	private Logger getLogger() {
		return Logger.getLogger( this.getClass() );
	}

	@Override
	public void opponentResigned() {

	}

	@Override
	public void setOpponent( Player opponent ) {
		this.opponent = opponent;
	}

	//injecting the position for tests, however maybe in future
	//it's useful for starting game from a non-initial position
	void setPosition( Position position ) {
		this.position = position;
	}
}
