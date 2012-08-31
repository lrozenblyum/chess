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
	private static final int MINIMAL_RANK = 1; //by FIDE
	private static final int MAXIMAL_RANK = 8; //by FIDE

	private static final int WHITE_PAWN_INITIAL_RANK = 2;
	private static final int BLACK_PAWN_INITIAL_RANK = 7;

	//by specification - the furthest from starting position
	//(in theory it means possibility to extend for fields others than 8*8)
	private static final int WHITE_PAWN_PROMOTION_RANK = MAXIMAL_RANK;
	private static final int BLACK_PAWN_PROMOTION_RANK = MINIMAL_RANK;


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
		String file = String.valueOf( square.charAt( 0 ) ); //depends on format e2

		//TODO: this internal conversion is needed because char itself has its
		//numeric value
		final int rank = Integer.valueOf( String.valueOf(square.charAt( 1 ) ));

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = squaresOccupied.get( square );

		final String rightCaptureSquare = fileToRight( file ) + getNextRank( rank, side );
		final String leftCaptureSquare = fileToLeft( file ) + getNextRank( rank, side );

		if ( rank == getRankBeforePromotion( side ) ) {
			addPromotionResult( result, file, side );

			if ( isOccupiedBy( rightCaptureSquare, side.opposite() ) ) {
				addPromotionResult( result, fileToRight( file ), side );
			}

			if ( isOccupiedBy( leftCaptureSquare, side.opposite() ) ) {
				addPromotionResult( result, fileToLeft( file ), side );
			}
		}
		else {
			result.add( file + getNextRank( rank, side ) );
			if ( rank == getInitialRank( side ) ) {
				result.add( file + getNextRank( getNextRank( rank, side ), side ) );
			}

			//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
			//because it simply cannot find 'i' file result - it's null... I don't like such side effects
			addIfOccupiedBy( result, rightCaptureSquare, side.opposite() );
			addIfOccupiedBy( result, leftCaptureSquare, side.opposite() );
		}

		if ( enPassantFile != null && rank == getEnPassantPossibleRank( side ) ) {
			if ( enPassantFile.equals( fileToRight( file ) ) ) {
				result.add( fileToRight( file ) + getNextRank( rank, side ) );
			}
			else if ( enPassantFile.equals( fileToLeft( file ) ) ){
				result.add( fileToLeft( file ) + getNextRank( rank, side ) );
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
		switch ( side ) {
			case WHITE:
				return 5;
			case BLACK:
				return 4;
			default:
				return sideNotSupported( side );
		}
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

	/**
	 * @param side
	 * @return rank from which next pawn move can reach promotion rank
	 */
	private static int getRankBeforePromotion( Side side ) {
		switch ( side ) {
			case WHITE :
				return WHITE_PAWN_PROMOTION_RANK - 1;
			case BLACK:
				return BLACK_PAWN_PROMOTION_RANK + 1;
			default:
				return sideNotSupported( side );
		}
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


	private static String fileToLeft( String file ) {
		//TODO: UGLY construction, need better!
		return String.valueOf( (char) ( file.charAt( 0 ) - 1 ) );
	}

	private static String fileToRight( String file ) {
		return String.valueOf( (char) ( file.charAt( 0 ) + 1 ) );
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

	public Position move( String squareFrom, String squareTo ) {
		return null;
	}
}
