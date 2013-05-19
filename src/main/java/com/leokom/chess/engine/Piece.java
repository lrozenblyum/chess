package com.leokom.chess.engine;

/**
 * Represent single piece of some specific side
 * (e.g. white rook, black pawn)
 */
class Piece {
	private final PieceType pieceType;
	private final Side side;

	public Piece( PieceType pieceType, Side side ) {
		this.pieceType = pieceType;
		this.side = side;
	}

	PieceType getPieceType() {
		return pieceType;
	}

	Side getSide() {
		return side;
	}

	public String toString() {
		return pieceType + ":" + side;
	}
}
