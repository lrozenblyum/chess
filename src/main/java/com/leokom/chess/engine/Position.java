package com.leokom.chess.engine;

import java.util.*;

/**
 * Current position on-board (probably with some historical data...)
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position {
	private static final int WHITE_PAWN_INITIAL_RANK = 2;
	private static final int BLACK_PAWN_INITIAL_RANK = 7;

	private static final int WHITE_PAWN_RANK_WHERE_PROMOTION_CAN_BE_DONE_IN_ONE_MOVE = 7;
	private static final String WHITE_PAWN_PROMOTION_RANK = "8";

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
	private Map< String, Side > sidesOccupied = new HashMap<String, Side>();


	/**
	 * Add a pawn to the position
	 * @param side
	 * @param square
	 */
	public void addPawn( Side side, String square ) {
		//TODO: what if the square is already occupied?
		sidesOccupied.put( square, side );
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
		final Side side = sidesOccupied.get( square );
		switch ( side ) {
			case WHITE:
				if ( rank == WHITE_PAWN_RANK_WHERE_PROMOTION_CAN_BE_DONE_IN_ONE_MOVE ) {
					for ( String pieceToPromote : PIECES_TO_PROMOTE_FROM_PAWN ) {
						result.add( file + WHITE_PAWN_PROMOTION_RANK + pieceToPromote );
					}
				}
				else {
					final int higherRank = rank + 1;
					result.add( file + higherRank );
					if ( rank == WHITE_PAWN_INITIAL_RANK ) {
						result.add( file + ( rank + 2 ) );
					}

					//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
					//because it simply cannot find 'i' file result - it's null... I don't like such side effects

					addIfOccupiedByBlack( result, fileToRight( file ) + higherRank );
					addIfOccupiedByBlack( result, fileToLeft( file ) + higherRank );
				}

				break;
			case BLACK:
				final int lowerRank = rank - 1;
				result.add( file + lowerRank );
				if ( rank == BLACK_PAWN_INITIAL_RANK ) {
					result.add( file + ( rank - 2 ) );
				}

				addIfOccupiedByWhite( result, fileToRight( file ) + lowerRank );
				addIfOccupiedByWhite( result, fileToLeft( file ) + lowerRank );

				break;
		}

		return result;

	}

	private String fileToLeft( String file ) {
		//TODO: UGLY construction, need better!
		return String.valueOf( (char) ( file.charAt( 0 ) - 1 ) );
	}

	private String fileToRight( String file ) {
		return String.valueOf( (char) ( file.charAt( 0 ) + 1 ) );
	}

	/**
	 * Add the square to result IFF it's occupied by black!
	 * @param result
	 * @param square
	 */
	private void addIfOccupiedByBlack( Set<String> result, String square ) {
		addIfOccupiedBy( result, square, Side.BLACK );
	}

	private void addIfOccupiedByWhite( Set<String> result, String square ) {
		addIfOccupiedBy( result, square, Side.WHITE );
	}

	private void addIfOccupiedBy( Set<String> result, String square, Side side ) {
		if ( sidesOccupied.get( square ) != null &&
				sidesOccupied.get( square ) == side ) {
			result.add( square );
		}
	}
}
