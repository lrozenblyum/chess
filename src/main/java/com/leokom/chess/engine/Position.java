package com.leokom.chess.engine;

import java.util.*;

/**
 * Current position on-board (probably with some historical data...)
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
		switch ( side ) {
			case WHITE:
				final int higherRank = rank + 1;
				final String rightCaptureSquare = fileToRight( file ) + higherRank;
				final String leftCaptureSquare = fileToLeft( file ) + higherRank;
				// -1 means - the move will reach the promotion rank if executed
				if ( rank == WHITE_PAWN_PROMOTION_RANK - 1 ) {
					addWhitePromotionResult( result, file );

					if ( isOccupiedBy( rightCaptureSquare, Side.BLACK ) ) {
						addWhitePromotionResult( result, fileToRight( file ) );
					}

					if ( isOccupiedBy( leftCaptureSquare, Side.BLACK ) ) {
						addWhitePromotionResult( result, fileToLeft( file ) );
					}
				}
				else {

					result.add( file + higherRank );
					if ( rank == WHITE_PAWN_INITIAL_RANK ) {
						result.add( file + ( rank + 2 ) );
					}

					//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
					//because it simply cannot find 'i' file result - it's null... I don't like such side effects
					addIfOccupiedBy( result, rightCaptureSquare, Side.BLACK );
					addIfOccupiedBy( result, leftCaptureSquare, Side.BLACK );
				}

				break;
			case BLACK:
				final int lowerRank = rank - 1;
				final String leftCaptureSquareForBlack = fileToLeft( file ) + lowerRank;
				final String rightCaptureSquareForBlack = fileToRight( file ) + lowerRank;

				// +1 means - black pawn will reach promoted rank if executed
				if ( rank == BLACK_PAWN_PROMOTION_RANK + 1 ) {
					addBlackPromotionResult( result, file );

					if ( isOccupiedBy( leftCaptureSquareForBlack, Side.WHITE ) ) {
						addBlackPromotionResult( result, fileToLeft( file ) );
					}

					if ( isOccupiedBy( rightCaptureSquareForBlack, Side.WHITE ) ) {
						addBlackPromotionResult( result, fileToRight( file ) );
					}
				}
				else {
					result.add( file + lowerRank );
					if ( rank == BLACK_PAWN_INITIAL_RANK ) {
						result.add( file + ( rank - 2 ) );
					}

					addIfOccupiedBy( result, rightCaptureSquareForBlack, Side.WHITE );
					addIfOccupiedBy( result, leftCaptureSquareForBlack, Side.WHITE );
				}

				break;

		}

		return result;

	}

	/**
	 * @see #addWhitePromotionResult(java.util.Set, String)
	 * the only difference it's black-specific method
	 *
	 * @param result
	 * @param file
	 */
	private static void addBlackPromotionResult( Set<String> result, String file ) {
		for ( String pieceToPromote : PIECES_TO_PROMOTE_FROM_PAWN ) {
			result.add( file + BLACK_PAWN_PROMOTION_RANK + pieceToPromote );
		}
	}

	/**
	 * Add to the result set all possible cases of promoting a white pawn in
	 * the file provided
	 * @param result result set to be modified
	 * @param file file - place of promotion
	 */
	private static void addWhitePromotionResult( Set<String> result, String file ) {
		for ( String pieceToPromote : PIECES_TO_PROMOTE_FROM_PAWN ) {
			result.add( file + WHITE_PAWN_PROMOTION_RANK + pieceToPromote );
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
		return squaresOccupied.get( square ) != null && squaresOccupied.get( square ) == side;
	}
}
