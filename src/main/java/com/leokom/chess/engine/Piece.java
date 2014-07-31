package com.leokom.chess.engine;

/**
 * Represent single IMMUTABLE piece of some specific side
 * (e.g. white rook, black pawn)
 */
public class Piece {
	private final PieceType pieceType;
	private final Side side;

	public Piece( PieceType pieceType, Side side ) {
		this.pieceType = pieceType;
		this.side = side;
	}

	public PieceType getPieceType() {
		return pieceType;
	}

	public Side getSide() {
		return side;
	}

	public String toString() {
		return pieceType + ":" + side;
	}
}
