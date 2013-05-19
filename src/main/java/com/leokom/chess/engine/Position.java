package com.leokom.chess.engine;

import java.util.*;

import static com.leokom.chess.engine.Board.*;
import static com.leokom.chess.engine.HorizontalDirection.*;

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
	private static final int WHITE_PAWN_PROMOTION_RANK = MAXIMAL_RANK;
	private static final int BLACK_PAWN_PROMOTION_RANK = MINIMAL_RANK;


	private static final String QUEEN = "Q";
	private static final String ROOK = "R";
	//NOTE: this name MUSTN'T be confused with anything religious.
	// It's just a common name for the piece which e.g. in Russian has name слон ('elephant')
	private static final String BISHOP = "B";
	private static final String KNIGHT = "N";
	//TODO: read carefully if this set is thread-safe
	private static final Set< String > PIECES_TO_PROMOTE_FROM_PAWN =
		Collections.unmodifiableSet(
			new HashSet< String >( Arrays.asList( QUEEN, ROOK,	BISHOP, KNIGHT ) )
	);

	/**
	 * Size of promotion move (e.g. "h1Q")
	 */
	private static final int PROMOTION_MOVE_SIZE = 3;

	private final Map< String, Side > queens = new HashMap<String, Side>();

	private final Map< String, Piece > pieces = new HashMap<String, Piece>();


	private final String enPassantFile;

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
		if ( !isSquareValid( square ) ) {
			throw new IllegalArgumentException( "Wrong destination square: " + square );
		}
		//TODO: what if the square is already occupied?
		pieces.put( square, new Piece( PieceType.PAWN, side ) );
	}

	private static final int VALID_SQUARE_LENGTH = 2;
	//may add some char/int validations. So far length is enough
	private boolean isSquareValid( String square ) {
		return square.length() == VALID_SQUARE_LENGTH;
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

		final String file = fileOfSquare( square );
		final int rank = rankOfSquare( square );

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = getPawnsSide( square );

		if ( rank == getRankBeforePromotion( side ) ) {
			addPromotionResult( result, file, side );

			for ( HorizontalDirection direction : HorizontalDirection.values() ) {
				String captureSquare = getPawnCaptureSquare( square, direction );
				if ( isOccupiedBy( captureSquare, side.opposite() ) ) {
					addPromotionResult( result, fileTo( file, direction ), side );
				}
			}
		}
		else {
			result.add( file + getNextRank( rank, side ) );
			if ( rank == getInitialRank( side ) ) {
				result.add( file + getDoubleMoveRank( side ) );
			}

			//TODO: need to check if we're NOT at a/h files, however test shows it's NOT Needed
			//because it simply cannot find 'i' file result - it's null... I don't like such side effects

			for ( HorizontalDirection direction : HorizontalDirection.values() ) {
				String captureSquare = getPawnCaptureSquare( square, direction );
				addIfOccupiedBy( result, captureSquare, side.opposite() );
			}
		}

		if ( enPassantFile != null && rank == getEnPassantPossibleRank( side ) ) {
			for ( HorizontalDirection direction : HorizontalDirection.values() ) {
				if ( enPassantFile.equals( fileTo( file, direction ) ) ) {
					result.add( fileTo( file, direction ) + getNextRank( rank, side ) );
				}
			}
		}

		result.removeAll( getImpossibleMovesForPawn( result, square ) );
		return result;
	}

	private String getPawnCaptureSquare( String pawnSquare, HorizontalDirection direction ) {
		final String file = fileOfSquare( pawnSquare );
		final int rank = rankOfSquare( pawnSquare );
		final Side side = getPawnsSide( pawnSquare );

		return fileTo( file, direction ) + getNextRank( rank, side );
	}

	/**
	 * Get set of moves from initial potentialPawnMoves
	 * that aren't allowed according to chess rules
	 * @param potentialPawnMoves moves that were detected as potential possibilities
	 * @param square current pawn position
	 * @return set of moves to be removed
	 */
	private Set<String> getImpossibleMovesForPawn( Set<String> potentialPawnMoves, String square ) {
		Set< String > disallowedMoves = new HashSet<String>();
		for ( String potentialMove : potentialPawnMoves ) {
			String destinationSquare = getDestinationSquare( potentialMove );

			//pawn cannot move to occupied square
			//if file is different - it's capture and should be allowed
			final boolean isMoveForward = sameFile( destinationSquare, square );
			if ( !isMoveForward ) {
				continue;
			}

			if ( isOccupied( destinationSquare ) ) {
				disallowedMoves.add( potentialMove );
			}

			Side side = getPawnsSide( square );
			int intermediateRank = getPawnDoubleMoveIntermediateRank( side );
			if ( rankOfSquare( destinationSquare ) == getDoubleMoveRank( side ) &&
				rankOfSquare( square ) == getInitialRank( side )
				&& isOccupied( fileOfSquare( square ) + intermediateRank ) ) {

				disallowedMoves.add( potentialMove );
			}
		}
		return disallowedMoves;
	}

	private Side getPawnsSide( String square ) {
		final Piece piece = pieces.get( square );
		if ( piece == null ) {
			return null; //TODO: correct? Some code relied on this.
		}
		if ( piece.getPieceType() != PieceType.PAWN ) {
			throw new IllegalArgumentException( "Square doesn't contain pawn: " + square );
		}
		return piece.getSide();
	}

	/**
	 * Get rank of pawn's double move intermediate square
	 * @param side side of pawn
	 * @return rank
	 */
	private static int getPawnDoubleMoveIntermediateRank( Side side ) {
		// does it look logical? 2+4-->3, 7+5-->6
		return ( getDoubleMoveRank( side ) + getInitialRank( side ) ) /2;
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
		//if not found is null -> null != side
		return hasPawn( side, square ) || hasQueen( side, square );
	}

	private boolean isOccupied( String square ) {
		return isOccupiedBy( square, Side.BLACK ) || isOccupiedBy( square, Side.WHITE );
	}

	private static int getDoubleMoveRank( Side side ) {
		return getNextRank( getNextRank( getInitialRank( side ), side ), side );
	}

	/**
	 * Make move from squareFrom
	 * We guarantee returning a new position instead of
	 * modifying the current one.
	 *
	 * The implementation should execute the move provided, guaranteeing that unaffected
	 * pieces must be kept on the same place
	 * (NOTE: unaffected is not so easy as can be imagined, e.g. when we move en passant the piece we capture
	 * IS affected! However it's not related to squareFrom and move)
	 * @param squareFrom square where the piece being moved exists BEFORE the move
	 * @param move either simply destination square (e.g. 'e4')
	 *             or destination square with capture info (e.g. 'h8Q')
	 * @return new position, which is received from current by making 1 move
	 */
	public Position move( String squareFrom, String move ) {
		//depending on format 'h8Q'
		final String squareTo = getDestinationSquare( move );

		final String newEnPassantFile = getNewEnPassantFile( squareFrom, squareTo );
		final Position result = new Position( newEnPassantFile );

		final Side movingSide = getPawnsSide( squareFrom );

		if ( isPromotion( move ) ) {
			result.addQueen( movingSide, squareTo );
		}

		final Collection<String> pawnsToCopy = getAllPawns();
		pawnsToCopy.remove( squareFrom );

		//en passant capture requires extra processing
		//because we capture a piece not being on the target square
		final String enPassantCapturedPawnSquare = getEnPassantCapturedPieceSquare( squareFrom, move );
		if ( enPassantCapturedPawnSquare != null ) {
			pawnsToCopy.remove( enPassantCapturedPawnSquare );
		}

		if ( !pawnsToCopy.isEmpty() ) {
			for ( final String busySquare : pawnsToCopy ) {
				result.addPawn( getPawnsSide( busySquare ), busySquare );
			}
		}

		//will work till we implement queens move...
		final Set<String> queensToCopy = queens.keySet(); //TODO: need copy?
		for ( String queen : queensToCopy ) {
			//TODO: this if looks ugly - just
			//to prevent very specific case:
			//promotion with capture of opposite queen
			if ( !queen.equals( squareTo ) ) {
				result.addQueen( queens.get( queen ), queen );
			}
		}

		//basing on current overwriting effect (must be the last),
		//to capture...
		if ( !isPromotion( move ) ) {
			result.addPawn( movingSide, squareTo );
		}

		return result;
	}

	private Set< String > getAllPawns() {
		Set< String > pawnSquares = new HashSet<String>();
		for ( String square : pieces.keySet() ) {
			if ( pieces.get( square ).getPieceType() == PieceType.PAWN ) {
				pawnSquares.add( square );
			}
		}

		return pawnSquares;
	}

	/**
	 * Get destination square from the move
	 *
	 * @param move in format like e2 or f1Q
	 * @return destination square (e2 or f1 correspondingly)
	 */
	private String getDestinationSquare( String move ) {
		return isPromotion( move ) ?
				move.substring( 0, 2 ) :
				move;
	}

	private static boolean isPromotion( String move ) {
		return move.length() == PROMOTION_MOVE_SIZE;
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
		int enPassantPossibleRank = getEnPassantPossibleRank( getPawnsSide( squareFrom ) );

		if ( this.enPassantFile != null &&
			rankOfSquare( squareFrom ) == enPassantPossibleRank &&
			this.enPassantFile.equals( fileOfSquare( squareTo ))) {
			return this.enPassantFile + enPassantPossibleRank;
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
	//TODO: when we'll implement other pieces moves - this must be executed
	//only for pawns!
	private String getNewEnPassantFile( String squareFrom, String squareTo ) {
		final Side side = getPawnsSide( squareFrom );

		return rankOfSquare( squareFrom ) == getInitialRank( side ) &&
				rankOfSquare( squareTo ) == getDoubleMoveRank( side ) ?
				fileOfSquare( squareFrom ) : null;
	}

	/**
	 * Check if the position has a pawn on square provided
	 * with needed side
	 *
	 * @param side
	 * @param square
	 * @return true iff such pawn is present
	 */
	boolean hasPawn( Side side, String square ) {
		return getPawnsSide( square ) == side;
	}

	//TODO: if this method is used in real production code
	//it probably requires test coverage. Now it's for tests only
	/**
	 * Check if square is empty (not occupied by any piece)
	 * @param square square to check
	 * @return true if square is empty
	 */
	boolean isEmptySquare( String square ) {
		return getPawnsSide( square ) == null && queens.get( square ) == null;
	}

	/**
	 * If previous move was done by pawn (double-step from initial position)
	 * returns the file of movement, otherwise null
	 * @return possible en passant file if double-move done
	 */
	String getPossibleEnPassantFile() {
		return this.enPassantFile;
	}

	public void addQueen( Side side, String square ) {
		queens.put( square, side );
	}

	public boolean hasQueen( Side side, String square ) {
		return queens.get( square ) == side;
	}

	//currently for tests only...
	@Override
	public String toString() {
		String wholePicture = "";
		for( String square : pieces.keySet() ) {
			wholePicture += pieces.get( square );
		}
		return wholePicture;
	}
}
