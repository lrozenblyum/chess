package com.leokom.games.chess.engine;

import java.util.Objects;

/**
 * Represent single IMMUTABLE piece of some specific side
 * (e.g. white rook, black pawn)
 * REFACTOR: to fully featured value object:
 * - no constructor but have some static returned valueOf
 * (will allow huge memory economy)
 *
*/
public final class Piece {
	private final PieceType pieceType;
	private final Side side;

	Piece( PieceType pieceType, Side side ) {
		this.pieceType = pieceType;
		this.side = side;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public Side getSide() {
		return side;
	}

	@Override
	public String toString() {
		return pieceType + ":" + side;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( !( obj instanceof Piece ) ) {
			return false;
		}

		Piece right = ( Piece ) obj;
		return ( this.side == right.side )&&
				( this.pieceType == right.pieceType );
	}

	@Override
	public int hashCode() {
		return Objects.hash( this.side, this.pieceType );
	}
}
