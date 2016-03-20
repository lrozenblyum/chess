package com.leokom.chess.engine;

import static com.leokom.chess.engine.Board.fileOfSquare;
import static com.leokom.chess.engine.Board.rankOfSquare;
import static com.leokom.chess.engine.Position.getDoubleMoveRank;
import static com.leokom.chess.engine.Position.getEnPassantPossibleRank;

/**
 * Create new positions after moves
 * @see InitialPosition#generate() for the way to generate
 * the initial position
 *
 * Author: Leonid
 * Date-time: 06.07.13 22:16
 */
final class PositionGenerator {
	private final Position source;

	PositionGenerator( Position source ) {
		this.source = source;
	}

	/**
	 * This method itself is a particularly-validating generator
	 * @param move move to be executed
	 * @return new position, after move from squareFrom
	 */
	Position generate( Move move ) {
		final Position position = getPosition( move );
		//post-processing actions that require analyzing at the latest stage
		if ( move != Move.OFFER_DRAW ) {
			position.setWaitingForAcceptDraw( false );
		}

		return position;
	}

	private Position getPosition( Move move ) {
		if ( source.getSideToMove() == Side.BLACK && source.getCountOfMovesToDraw() != 0 ) {
			return createTerminalPosition( null );
		}

		if ( move == Move.RESIGN ) {
			return createTerminalPosition( source.getSideToMove().opposite() );
		}

		if ( move == Move.OFFER_DRAW ) {
			return getOfferDrawPosition();
		}

		if ( move == Move.ACCEPT_DRAW ) {
			return getAcceptDrawPosition();
		}

		validateStandardMove( move );

		String squareFrom = move.getFrom();
		String moveTo = move.getTo();
		final PieceType pieceType = source.getPieceType( squareFrom );
		switch ( pieceType ) {
			case KNIGHT:
			case BISHOP:
			case QUEEN:
				return processMoveWithoutSideEffects( squareFrom, moveTo );
			case ROOK:
				return processRookMove( squareFrom, moveTo );
			case KING:
				return processKingMove( squareFrom, moveTo );
			case PAWN:
				return processPawnMove( squareFrom, moveTo );
			default:
				throw new IllegalArgumentException( "There are no other chess pieces. Received: " + pieceType );
		}
	}

	private Position getOfferDrawPosition() {
		//no side to move change after draw offer
		Position result = new Position( source.getSideToMove() );
		source.copyStateTo( result );
		result.setWaitingForAcceptDraw( true );
		return result;
	}

	public Position getAcceptDrawPosition() {
		return createTerminalPosition( null );
	}

	private void validateStandardMove( Move move ) {
		if ( move == null ) {
			throw new IllegalArgumentException( "Move must be not null" );
		}

		if ( source.getPiece( move.getFrom() ) == null ) {
			throw new IllegalArgumentException( "Source square is empty : " + move );
		}

		if ( source.getPiece( move.getFrom() ).getSide() != source.getSideToMove() ) {
			throw new IllegalArgumentException( "Wrong side to move : " + move + ". Currently it's turn of " + source.getSideToMove() );
		}
	}

	private Position createTerminalPosition( Side winningSide ) {
		final Position result = new Position( null );
		source.copyStateTo( result );
		//TODO: should checkmate move also set this flag?
		result.setTerminal( winningSide );
		return result;
	}

	private Position processRookMove( String squareFrom, String move ) {
		final Position result = processMoveWithoutSideEffects( squareFrom, move );
		//TODO: do I need setting this flag after castling?
		//for consistency: yes, for algorithm: no

		//TODO: technically it will do excessive 'again' setting (not harmful)
		//if the rook has already moved
		if ( Board.fileOfSquare( squareFrom ).equals( "a" ) ) {
			result.setHasARookMoved( this.source.getSide( squareFrom ) );
		}

		if ( Board.fileOfSquare( squareFrom ).equals( "h" ) ) {
			result.setHasHRookMoved( this.source.getSide( squareFrom ) );
		}
		return result;
	}

	private Position processKingMove( String squareFrom, String move ) {
		Position newPosition = processMoveWithoutSideEffects( squareFrom, move );

		//since we're NOT validating move possibility, e1-g1 for King means only one:
		//white castling king-side
		final boolean isCastlingKingSide =
				( squareFrom.equals( "e1" ) && move.equals( "g1" ) ) ||
				( squareFrom.equals( "e8" ) && move.equals( "g8" ) );

		if ( isCastlingKingSide ) {
			int rank = Board.rankOfSquare( squareFrom );
			//rook movement
			newPosition.moveUnconditionally( "h" + rank, "f" + rank );
		}

		//since we're NOT validating move possibility, e1-c1 for King means only one:
		//white castling queen-side
		final boolean isCastlingQueenSide =
			( squareFrom.equals( "e8" ) && move.equals( "c8" ) ) ||
			( squareFrom.equals( "e1" ) && move.equals( "c1" ) );

		if ( isCastlingQueenSide ) {
			int rank = Board.rankOfSquare( squareFrom );
			newPosition.moveUnconditionally( "a" + rank, "d" + rank );
		}

		newPosition.setHasKingMoved( this.source.getSide( squareFrom ) );
		return newPosition;
	}

	private Position processPawnMove( String squareFrom, String move ) {
		final String squareTo = Move.getDestinationSquare( move );

		final String newEnPassantFile = getNewEnPassantFile( squareFrom, squareTo );


		final Side movingSide = source.getSideToMove();

		final Position result = new Position( movingSide.opposite() );
		result.setEnPassantFile( newEnPassantFile );
		cloneAndRemove( result, squareFrom );

		//en passant capture requires extra processing
		//because we capture a piece not being on the target square
		final String enPassantCapturedPawnSquare = getEnPassantCapturedPieceSquare( squareFrom, move );
		if ( enPassantCapturedPawnSquare != null ) {
			result.removePiece( enPassantCapturedPawnSquare );
		}

		if ( Move.isPromotion( move ) ) {
			PieceType promotedPieceType = Move.getPromotionPieceType( move );
			result.add( movingSide, squareTo, promotedPieceType );
		} else {
			//if it's capture - also ok - as it overwrites....
			result.addPawn( movingSide, squareTo );
		}

		return result;
	}

	/**
	 * Generate new position from current position, not assuming any side effects
	 * The piece is just moved from current square to the destination.
	 *
	 * En passant possibility is cleared (it's not pawn)
	 *
	 * @param squareFrom square from
	 * @param move move to execute (actually here it's just a square...)
	 * @return new position
	 */
	private Position processMoveWithoutSideEffects( String squareFrom, String move ) {
		//after moving everything except a pawn
		//we don't set (thus clearing) the flag about en passant possibility
		final Position position = new Position( source.getSideToMove().opposite() );
		cloneAndRemove( position, squareFrom );

		position.add( source.getSide( squareFrom ), move, source.getPieceType( squareFrom ) );

		return position;
	}

	/**
	 * Clone newPosition.
	 * Remove the piece from the initial square
	 * @param newPosition target of cloning
	 * @param squareFrom will be empty in the newPosition
	 */
	private void cloneAndRemove( Position newPosition, String squareFrom ) {
		source.copyStateTo( newPosition );

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

		return rankOfSquare( squareFrom ) == InitialPosition.getPawnInitialRank( side ) &&
				rankOfSquare( squareTo ) == getDoubleMoveRank( side ) ?
				fileOfSquare( squareFrom ) : null;
	}
}
