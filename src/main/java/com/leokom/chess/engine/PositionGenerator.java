package com.leokom.chess.engine;

import static com.leokom.chess.engine.Board.fileOfSquare;
import static com.leokom.chess.engine.Board.rankOfSquare;
import static com.leokom.chess.engine.Position.*;

/**
 * Create new positions after moves
 * Author: Leonid
 * Date-time: 06.07.13 22:16
 */
final class PositionGenerator {
	private final Position source;

	PositionGenerator( Position source ) {
		this.source = source;
	}

	Position generate( String squareFrom, String move ) {
		final PieceType pieceType = source.getPieceType( squareFrom );
		switch ( pieceType ) {
			case KNIGHT:
			case BISHOP:
			case ROOK:
			case QUEEN:
			case KING:
				//after moving everything except a pawn
				//the flag about en passant possibility must be cleared
				final String newEnPassantFile = null;
				final Position position = new Position( newEnPassantFile );
				cloneAndRemove( position, squareFrom );

				position.add( source.getSide( squareFrom ), move, pieceType );

				return position;
		}

		final String squareTo = Move.getDestinationSquare( move );

		final String newEnPassantFile = getNewEnPassantFile( squareFrom, squareTo );

		final Position result = new Position( newEnPassantFile );
		cloneAndRemove( result, squareFrom );

		//en passant capture requires extra processing
		//because we capture a piece not being on the target square
		final String enPassantCapturedPawnSquare = getEnPassantCapturedPieceSquare( squareFrom, move );
		if ( enPassantCapturedPawnSquare != null ) {
			result.removePiece( enPassantCapturedPawnSquare );
		}

		final Side movingSide = source.getSide( squareFrom );

		if ( Move.isPromotion( move ) ) {
			//depends on 3-char format
			String promotionNotation = move.substring( 2 );
			PieceType promotedPieceType = PieceType.byNotation( promotionNotation );
			result.add( movingSide, squareTo, promotedPieceType );
		} else {
			//if it's capture - also ok - as it overwrites....
			result.addPawn( movingSide, squareTo );
		}

		return result;
	}

	/**
	 * Clone newPosition.
	 * Remove the piece from the initial square
	 * @param newPosition target of cloning
	 * @param squareFrom will be empty in the newPosition
	 */
	private void cloneAndRemove( Position newPosition, String squareFrom ) {
		source.copyPiecesInto( newPosition );

		newPosition.removePiece( squareFrom );
	}

	/**
	 * Get square where the en-passant captured piece is on
	 * (null if we are not doing en passant)
	 * @param squareFrom initial pawns square
	 * @param squareTo target pawn square
	 * @return en-passant captured piece's square (or null)
	 */
	private String getEnPassantCapturedPieceSquare( String squareFrom, String squareTo ) {
		//rank only from which a pawn can execute en passant move
		//(it's equal to rank where the opposite piece being captured is on)
		int enPassantPossibleRank = getEnPassantPossibleRank( source.getSide( squareFrom ) );

		if ( source.getPossibleEnPassantFile() != null &&
				rankOfSquare( squareFrom ) == enPassantPossibleRank &&
				source.getPossibleEnPassantFile().equals( fileOfSquare( squareTo ) )) {
			return source.getPossibleEnPassantFile() + enPassantPossibleRank;
		}
		return null;
	}


	/**
	 * Get a file for new position, for which the next squareTo could be en passant
	 * (if possible)
	 * @param squareFrom square from which the piece is going to squareTo
	 * @param squareTo square to which the piece is going to squareTo
	 * @return possible en passant file (null if impossible)
	 */
	private String getNewEnPassantFile( String squareFrom, String squareTo ) {
		final Side side = source.getSide( squareFrom );

		return rankOfSquare( squareFrom ) == getPawnInitialRank( side ) &&
				rankOfSquare( squareTo ) == getDoubleMoveRank( side ) ?
				fileOfSquare( squareFrom ) : null;
	}
}
