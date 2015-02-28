package com.leokom.chess.engine;


/**
 * Create position in fluent-interface style
 *
 *
 * Author: Leonid
 * Date-time: 28.02.15 22:05
 */
public class PositionBuilder {
	final Position position;

	public PositionBuilder() {
		position = new Position();
	}

	public PositionBuilder add( Side side, String square, PieceType pieceType ) {
		position.add( side, square, pieceType );
		return this;
	}

	public PositionBuilder setSideOf( String square ) {
		position.setSideToMove( position.getSide( square ) );
		return this;
	}

	public Position build() {
		return position;
	}
}
