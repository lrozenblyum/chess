package com.leokom.chess.engine;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 *
 * Easy way to iterate different moves from a position,
 * and do some actions after moves
 *
 * Author: Leonid
 * Date-time: 23.03.16 22:16
 */
class PositionMover {
	private final Stream<Move> moves;
	private final AtomicReference<Position> position;
	private Consumer<Position> actionAfterMove;

	PositionMover( Position position, Stream<Move> moves ) {
		//trick to overcome impossibility to mutate inside lambda
		//http://stackoverflow.com/a/32768790/1429367
		this.position = new AtomicReference<>( position );
		this.moves = moves;
	}

	PositionMover afterMove( Consumer<Position> actionAfterMove ) {
		this.actionAfterMove = actionAfterMove;
		return this;
	}

	Position run() {
		this.moves.forEach( move -> {
			doMove( move );

			if ( actionAfterMove != null ) {
				actionAfterMove.accept( position.get() );
			}
		} );

		return position.get();
	}

	private void doMove( Move move ) {
		position.set( position.get().move( move ) );
	}

}
