package com.leokom.chess.engine;

import java.util.*;

/**
 * Current position on-board (probably with some historical data...)
 *
 * I consider the position as immutable (however this vision may change
 * by taking into account historical data)
 *
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position {
	private static final int WHITE_PAWN_INITIAL_RANK = 2;
	private static final int BLACK_PAWN_INITIAL_RANK = 7;

	//by specification - the furthest from starting position
	//(in theory it means possibility to extend for fields others than 8*8)
	private static final int WHITE_PAWN_PROMOTION_RANK = Board.MAXIMAL_RANK;
	private static final int BLACK_PAWN_PROMOTION_RANK = Board.MINIMAL_RANK;

	//TODO: read carefully if this set is thread-safe
	private static final Set< String > PIECES_TO_PROMOTE_FROM_PAWN =
			Collections.unmodifiableSet(
				new HashSet< String >(
					Arrays.asList(
						"Q", //queen
						"R", //rook
						"B", //bishop. NOTICE: this name MUSTN'T be confused with anything religious.
							// It's just a common name for the piece which e.g. in Russian has name слон ('elephant')
						"N" //knight
			)
		)
	);

	/**
	 * square -> side
	 */
	private Map< String, Side > squaresOccupied = new HashMap<String, Side>();

	private String enPassantFile;

	//TODO: in theory the flag could be inconsistent with actual position...
	//maybe need some builder?
	public Position( String enPassantFile ) {
		this.enPassantFile = enPassantFile;
	}


	/**
	 * Add a pawn to the position
	 * @param side
	 * @param square
	 */
	public void addPawn( Side side, String square ) {
		//TODO: what if the square is already occupied?
		squaresOccupied.put( square, side );
	}

	/**
	 * Get moves that are available from the square provided
	 * @param square square currently in format like 'e2' (this we'll call further as 'canonical representation')
	 * @return not-null set of available moves from square (could be empty for sure)
	 *
	 * Move is now interpreted as following:
	 * 1) square's canonical representation if it's univocal (e.g. any pawn move including capture except promotion)
	 * While capture is usually indicated as x, from POV of single position it doesn't matter -
	 * the destination field correctly determines the result position in this case
	 * 2) square's canonical representation + upper-case of promoted piece from pawn (e.g. a8N -
	 * if we promoted to Knight)
	 *
	 * TODO: what if square doesn't contain any pieces?
	 */
	public Set<String> getMovesFrom( String square ) {
		final Set<String> result = new HashSet<String>();

		final String file = Board.fileOfSquare( square );
		final int rank = Board.rankOfSquare( square );

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = squaresOccupied.get( square );

		final String rightCaptureSquare = Board.fileToRight( file ) + getNextRank( rank, side );
		final String leftCaptureSquare = Board.fileToLeft( file ) + getNextRank( rank, side );

		if ( rank == getRankBeforePromotion( side ) ) {
			addPromotionResult( result, file, side );

			if ( isOccupiedBy( rightCaptureSquare, side.opposite() ) ) {
				addPromotionResult( result, Board.fileToRight( file ), side );
			}

			if ( isOccupiedBy( leftCaptureSquare, side.opposite() ) ) {
				addPromotionResult( result, Board.fileToLeft( file ), side );
			}
		}
		else {
			result.add( file + getNextRank( rank, side ) );
			if ( rank == getInitialRank( side ) ) {
				result.add( file + getDoubleMoveRank( side ) );
			}

			//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
			//because it simply cannot find 'i' file result - it's null... I don't like such side effects
			addIfOccupiedBy( result, rightCaptureSquare, side.opposite() );
			addIfOccupiedBy( result, leftCaptureSquare, side.opposite() );
		}

		if ( enPassantFile != null && rank == getEnPassantPossibleRank( side ) ) {
			if ( enPassantFile.equals( Board.fileToRight( file ) ) ) {
				result.add( Board.fileToRight( file ) + getNextRank( rank, side ) );
			}
			else if ( enPassantFile.equals( Board.fileToLeft( file ) ) ){
				result.add( Board.fileToLeft( file ) + getNextRank( rank, side ) );
			}
		}

		return result;
	}

	/**
	 * Get rank staying on which a pawn can execute
	 * en passant capture
	 * if previous move from another side was a double pawn move
	 * @param side
	 * @return rank with en passant possibility
	 */
	private static int getEnPassantPossibleRank( Side side ) {
		return getDoubleMoveRank( side.opposite() );
	}

	//TODO: the switches are smell about inheritance for PawnMovement!
	private static int getInitialRank( Side side ) {
		switch ( side ) {
			case WHITE:
				return WHITE_PAWN_INITIAL_RANK;
			case BLACK:
				return BLACK_PAWN_INITIAL_RANK;
			default:
				return sideNotSupported( side );
		}
	}

	/**
	 * Get pawn rank that is reachable from current rank by SINGLE move
	 * @param pawnRank
	 * @param side pawn side
	 * @return pawn rank
	 */
	private static int getNextRank( int pawnRank, Side side ) {
		switch ( side ) {
			case WHITE:
				return pawnRank + 1;
			case BLACK:
				return pawnRank - 1;
			default:
				return sideNotSupported( side );
		}
	}

	private static int getPreviousRank( int pawnRank, Side side ) {
		switch ( side ) {
			case WHITE:
				return pawnRank - 1;
			case BLACK:
				return pawnRank + 1;
			default:
				return sideNotSupported( side );
		}
	}

	/**
	 * @param side
	 * @return rank from which next pawn move can reach promotion rank
	 */
	private static int getRankBeforePromotion( Side side ) {
		return getPreviousRank( getPromotionRank( side ), side );
	}

	private static int getPromotionRank( Side side ) {
		switch ( side ) {
			case WHITE:
				return WHITE_PAWN_PROMOTION_RANK;
			case BLACK:
				return BLACK_PAWN_PROMOTION_RANK;
			default:
				return sideNotSupported( side );
		}
	}

	//TODO: boring check just for compiler, better choices?

	private static int sideNotSupported( Side side ) {
		throw new IllegalArgumentException( "Side is not supported: " + side );
	}

	/**
	 * Add to the result set all possible cases of promoting a pawn
	 * of given side in the file provided
	 * @param result result set to be modified
	 * @param file file place of promotion
	 * @param side side of pawn
	 */
	private static void addPromotionResult( Set<String> result, String file, Side side ) {
		for ( String pieceToPromote : PIECES_TO_PROMOTE_FROM_PAWN ) {
			result.add( file + getPromotionRank( side ) + pieceToPromote );
		}
	}

	/**
	 * Add the square to result IFF it's occupied by the side provided!
	 * @param result
	 * @param square
	 * @param side
	 */
	private void addIfOccupiedBy( Set<String> result, String square, Side side ) {
		if ( isOccupiedBy( square, side ) ) {
			result.add( square );
		}
	}

	/**
	 * Check if square provided is occupied by the side
	 * @param square
	 * @param side
	 * @return true if square is occupied by the side, false otherwise
	 * (means NOT occupied or occupied by the opposite side)
	 */
	private boolean isOccupiedBy( String square, Side side ) {
		return ( squaresOccupied.get( square ) != null ) &&( squaresOccupied.get( square ) == side );
	}

	private static int getDoubleMoveRank( Side side ) {
		return getNextRank( getNextRank( getInitialRank( side ), side ), side );
	}

	/**
	 * Perform move from squareFrom to squareTo
	 * We guarantee returning a new position instead of
	 * modifying the current one.
	 *
	 * The implementation should execute the move provided, guaranteeing that unaffected
	 * pieces must be left on the same place
	 * (NOTE: unaffected is not so easy as can be imagined, e.g. when we move en passant the piece we capture
	 * IS affected! However it's not related to squareFrom and squareTo)
	 * @param squareFrom
	 * @param squareTo
	 * @return new position, which is received from current by doing 1 move
	 */
	public Position move( String squareFrom, String squareTo ) {
		final String newEnPassantFile = getNewEnPassantFile( squareFrom, squareTo );
		final Position result = new Position( newEnPassantFile );

		final Collection<String> copySet = new HashSet<String>( squaresOccupied.keySet() );
		copySet.remove( squareFrom );

		int someNumber = squaresOccupied.get( squareFrom ) == Side.WHITE ? 5 : 4;
		String enPassantCapturedPieceSquare = null;
		if ( this.enPassantFile != null &&
			Board.rankOfSquare( squareFrom ) == someNumber &&
			this.enPassantFile.equals( Board.fileOfSquare( squareTo ))) {
			enPassantCapturedPieceSquare = this.enPassantFile + someNumber;
		}

		if ( !copySet.isEmpty() ) {
			for ( String busySquare : copySet ) {
				if ( allowAddingPawnToResult( enPassantCapturedPieceSquare, busySquare ) ) {
					result.addPawn( squaresOccupied.get( busySquare ), busySquare );
				}
			}
		}

		//basing on current overwriting effect (must be the last),
		//to capture...
		result.addPawn( squaresOccupied.get( squareFrom ), squareTo );

		return result;
	}

	private static boolean allowAddingPawnToResult( String enPassantCapturedPieceSquare, String busySquare ) {
		if ( enPassantCapturedPieceSquare == null ) {
			return true;
		}

		return !busySquare.equals( enPassantCapturedPieceSquare );
	}

	/**
	 * Get a file for new position, for which the next move could be en passant
	 * (if possible)
	 * @param squareFrom square from which the piece is going to move
	 * @param squareTo square to which the piece is going to move
	 * @return possible en passant file (null if impossible)
	 */
	private String getNewEnPassantFile( String squareFrom, String squareTo ) {
		final Side side = squaresOccupied.get( squareFrom );

		return Board.rankOfSquare( squareFrom ) == getInitialRank( side ) &&
				Board.rankOfSquare( squareTo ) == getDoubleMoveRank( side ) ?
				Board.fileOfSquare( squareFrom ) : null;
	}

	/**
	 * Check if the position has a pawn on square provided
	 * with needed side
	 * @param square
	 * @param side
	 * @return true iff such pawn is present
	 */
	boolean hasPawn( String square, Side side ) {
		return isOccupiedBy( square, side );
	}

	//TODO: if this method is used in real production code
	//it probably requires test coverage. Now it's for tests only
	/**
	 * Check if square is empty (not occupied by any piece)
	 * @param square square to check
	 * @return true if square is empty
	 */
	boolean isEmptySquare( String square ) {
		return squaresOccupied.get( square ) == null;
	}

	/**
	 * If previous move was done by pawn (double-step_ from initial position
	 * returns the file of movement, otherwise null
	 * @return possible en passant file if double-move done
	 */
	public String getPossibleEnPassantFile() {
		return this.enPassantFile;
	}
}
