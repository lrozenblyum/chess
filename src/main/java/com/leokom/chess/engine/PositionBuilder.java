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
		//TODO: not the best decision to hard-code side here
		//better delay position construction till build() method call
		position = new Position( Side.WHITE );
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

	public PositionBuilder addPawn( Side side, String square ) {
		position.addPawn( side, square );
		return this;
	}

	public PositionBuilder addQueen( Side side, String square ) {
		position.addQueen( side, square );
		return this;
	}

	public Position move( String from, String to ) {
		setSideOf( from );
		return position.move( from, to );
	}

	public PositionBuilder setEnPassantFile( String enPassantFile ) {
		position.setEnPassantFile( enPassantFile );
		return this;
	}
}
