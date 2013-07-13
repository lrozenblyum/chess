package com.leokom.chess.engine;

import com.leokom.chess.utils.CollectionUtils;

import java.util.*;

import static com.leokom.chess.engine.Board.*;
import static com.leokom.chess.utils.CollectionUtils.addIfNotNull;

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

	private static final Map< Side, Integer > PAWN_INITIAL_RANKS = new HashMap<Side, Integer>() { {
		put( Side.WHITE, WHITE_PAWN_INITIAL_RANK );
		put( Side.BLACK, BLACK_PAWN_INITIAL_RANK );
	}};

	//TODO: thread-safety for read-only purposes?
	private static final Map< Side, Integer > PAWN_PROMOTION_RANKS = new HashMap<Side, Integer>() { {
		put( Side.WHITE, WHITE_PAWN_PROMOTION_RANK );
		put( Side.BLACK, BLACK_PAWN_PROMOTION_RANK );
	}};

	//TODO: read carefully if this set is thread-safe
	private static final Set< PieceType > PIECES_TO_PROMOTE_FROM_PAWN =
		Collections.unmodifiableSet( EnumSet.of(
				PieceType.QUEEN, PieceType.ROOK,
				PieceType.KNIGHT, PieceType.BISHOP ) );


	//all pieces currently present on the board
	private final Map< String, Piece > pieces = new HashMap<String, Piece>();

	private final String enPassantFile;

	//TODO: in theory the flag could be inconsistent with actual position...
	//maybe need some builder?
	public Position( String enPassantFile ) {
		this.enPassantFile = enPassantFile;
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
	 * Should we return empty set, null or throw an exception?
	 */
	public Set<String> getMovesFrom( String square ) {
		switch ( getPieceType( square ) ) {
			case KNIGHT:
				return getKnightMoves( square );
			case BISHOP:
				return getBishopMoves( square );
			case ROOK:
				return getRookMoves( square );
			case QUEEN:
				return getQueenMoves( square );
			case KING:
				return getKingMoves( square );
			case PAWN:
				return getPawnMoves( square );
			default:
				//cannot cover?
				throw new IllegalArgumentException( "Unsupported piece type: " + getPieceType( square ) );

		}
	}

	private Set<String> getKingMoves( String square ) {
		//TODO: think, King's movement
		//is similar to Queen (in part of all directions)
		//could we reuse queen's movement?

		Set< String > result = new HashSet<String>();

		//diagonally
		for ( HorizontalDirection horizontalDirection : HorizontalDirection.values() ) {
			for ( VerticalDirection verticalDirection : VerticalDirection.values() ) {
				addIfNotNull( result, squareDiagonally( square, horizontalDirection, verticalDirection ) );
			}
		}

		//left/right/top/bottom
		for ( Direction direction : Direction.values() ) {
			addIfNotNull( result, squareTo( square, direction ) );
		}

		Side ourSide = getSide( square );
		Set< String > toRemove = new HashSet<String>();
		for ( String potentialSquare : result ) {
			//if occupied by another side - can capture
			if ( isOccupiedBy( potentialSquare, ourSide ) ) {
				toRemove.add( potentialSquare );
			}
		}

		result.removeAll( toRemove );

		//removing attack targets.

		Set< String > squaresWhereKingWillBeAttacked = new HashSet<String>();
		for ( String potentialMove : result ) {
			//will work in assumption that .move isn't validating!
			final Position potentialNewPosition = this.move( square, potentialMove );

			//they  may differ from move to move, e.g. when King performs a capture!
			//TODO: technically I can't find currently a test case that will be red
			//if I move the opponentPieces getting outside the loop (just from current position).
			//So theoretically it might be a performance improvement
			//the reason is that if we capture by king,
			//getting attacked squares from that square will get other squares, but not that one
			//where king resides
			//however I don't want to introduce this bad dependency on the side effect
			final Set< String > opponentPieces = potentialNewPosition.getOpponentPieces( ourSide );

			for ( String opponentPiece : opponentPieces ) {
				if ( potentialNewPosition.getSquaresAttackedFromSquare( opponentPiece ).contains( potentialMove ) ) {
					squaresWhereKingWillBeAttacked.add( potentialMove );
					break; //even may break the outer loop
				}
			}
		}

		result.removeAll( squaresWhereKingWillBeAttacked );

		return result;
	}

	private Set< String > getSquaresAttackedFromSquare( String chessSquare ) {
		//assuming square is occupied...
		switch ( pieces.get( chessSquare ).getPieceType() ) {
			case PAWN:
				return getSquaresAttackedByPawn( chessSquare );
			case KNIGHT:
				return getSquaresAttackedByKnight( chessSquare );
			case BISHOP:
				return getSquaresAttackedByBishop( chessSquare );
			case ROOK:
				return getSquaresAttackedByRook( chessSquare );
		 	default:
				//NOTE: throwing an exception so far breaks other tests
				return new HashSet<String>();
		}
	}

	private Set<String> getSquaresAttackedByRook( String square ) {
		Set< String > result = new HashSet<String>();

		for ( Direction direction : Direction.values() ) {
			String runningSquare = square;

			do {
				runningSquare = squareTo( runningSquare, direction );
				addIfNotNull( result, runningSquare );
			} while ( runningSquare != null && isEmptySquare( runningSquare ) );
		}

		return result;
	}

	private Set< String > getOpponentPieces( Side ourSide ) {
		Set< String > result = new HashSet< String >();
		for( String square : pieces.keySet() ) {
			if ( pieces.get( square ).getSide() == ourSide.opposite() ) {
				result.add( square );
			}
		}

		return result;
	}

	private Set< String > getKnightMoves( String square ) {
		final Set<String> knightMoves = getSquaresAttackedByKnight( square );
		//3.1. It is not permitted to move a piece to a square occupied by a piece of the same colour
		knightMoves.removeAll( getSquaresOccupiedByOurSide( knightMoves, getSide( square ) ) );
		return knightMoves;
	}


	private Set< String > getRookMoves( String square ) {
		final Set<String> result = getSquaresAttackedByRook( square );
		//3.1. It is not permitted to move a piece to a square occupied by a piece of the same colour
		result.removeAll( getSquaresOccupiedByOurSide( result, getSide( square ) ) );
		return result;
	}

	private Set< String > getBishopMoves( String square ) {
		Set< String > result = getSquaresAttackedByBishop( square );

		//it might be not very efficient since only the 'end' squares
		//must be checked like it was before
		//but I shouldn't increase complexity by cost of performance (theoretical) improvement
		result.removeAll( getSquaresOccupiedByOurSide( result, getSide( square ) ) );

		return result;
	}

	private Set<String> getQueenMoves( String square ) {
		//TODO: this works in assumption
		//that rook's castling is NOT included into
		//getRookMoves. Castling is considered as King's move
		final Set<String> rookMoves = getRookMoves( square );
		final Set< String > bishopMoves = getBishopMoves( square );
		final Set< String > result = new HashSet<String>();
		//TODO: some Guava/CollectionUtils for simplification?
		result.addAll( rookMoves );
		result.addAll( bishopMoves );

		return result;
	}


	private Set<String> getSquaresAttackedByBishop( String square ) {
		Set< String > result = new HashSet<String>();

		for ( HorizontalDirection horizontalDirection : HorizontalDirection.values() ) {
			for ( VerticalDirection verticalDirection : VerticalDirection.values() ) {
				String movingSquare = square;
				do {
					movingSquare = squareDiagonally( movingSquare, horizontalDirection, verticalDirection );
					//null means end of table reached and will break the loop
					addIfNotNull( result, movingSquare );
				}
				//the loop will include first busy square on its way to the result
				while ( movingSquare != null && isEmptySquare( movingSquare ) );
			}
		}
		return result;
	}

	//NOTE: from point of view of en passant we
	//still have the square diagonally-front attacked
	private Set< String > getSquaresAttackedByPawn( String square ) {
		Set< String > result = new HashSet<String>();
		for ( HorizontalDirection horizontalDirection : HorizontalDirection.values() ) {
			final Side side = getSide( square );

			final String attackedSquare = squareDiagonally( square, horizontalDirection, getPawnMovementDirection( side ) );

			addIfNotNull( result, attackedSquare );
		}
		return result;
	}

	private Set<String> getPawnMoves( String square ) {
		final Set<String> result = new HashSet<String>();

		final String file = fileOfSquare( square );
		final int rank = rankOfSquare( square );

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = getSide( square );

		if ( rank == getRankBeforePromotion( side ) ) {
			addPromotionResult( result, file, side );

			final Set<String> attacked = getSquaresAttackedByPawn( square );
			for ( String attackedSquare : attacked ) {
				if ( isOccupiedBy( attackedSquare, side.opposite() ) ) {
					addPromotionResult( result, fileOfSquare( attackedSquare ), side );
				}
			}
		}
		else {
			result.add( file + getPawnNextRank( rank, side ) );
			if ( rank == getPawnInitialRank( side ) ) {
				result.add( file + getDoubleMoveRank( side ) );
			}

			final Set<String> attacked = getSquaresAttackedByPawn( square );
			for ( String attackedSquare : attacked ) {
				if ( isOccupiedBy( attackedSquare, side.opposite() ) ) {
					result.add( attackedSquare );
				}

				//3.7 d. A pawn attacking a square crossed by an opponent’s pawn which has advanced two squares
				// in one move from its original square may capture this opponent’s pawn
				// as though the latter had been moved only one square
				if ( enPassantFile != null &&
					attackedSquare.equals( enPassantFile + getPawnDoubleMoveIntermediateRank( side.opposite() ) )) {
					result.add( attackedSquare );
				}
			}
		}

		result.removeAll( getImpossibleMovesForPawn( result, square ) );
		return result;
	}

	private Set<String> getSquaresAttackedByKnight( String square ) {
		//shifts pairs: horizontal and vertical shift
		//they will be combined with all possible vertical/horizontal directions
		int [][] shifts = new int[][] { {1, 2}, {2, 1} };

		Set< String > knightMoves = new HashSet<String>();
		for ( int [] shiftPair : shifts ) {
			for ( HorizontalDirection horizontalDirection : HorizontalDirection.values() ) {
				for ( VerticalDirection verticalDirection : VerticalDirection.values() ) {
					final String destination = Board.squareTo( square, horizontalDirection, shiftPair[ 0 ],
							verticalDirection, shiftPair[ 1 ] );

					//can be null when outside the board
					CollectionUtils.addIfNotNull( knightMoves, destination );
				}
			}
		}



		return knightMoves;
	}

	//TODO: it's very generic - since the rule 3.1 is common. Could we reuse it?
	private Set< String > getSquaresOccupiedByOurSide( Set<String> potentialMoves, Side ourSide ) {
		Set< String > result = new HashSet<String>();
		for ( String potentialMove : potentialMoves ) {
			if ( isOccupiedBy( potentialMove, ourSide ) ) {
				result.add( potentialMove );
			}
		}

		return result;
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
			String destinationSquare = Move.getDestinationSquare( potentialMove );

			//pawn cannot move to occupied square
			//if file is different - it's capture and should be allowed
			final boolean isMoveForward = sameFile( destinationSquare, square );
			if ( !isMoveForward ) {
				continue;
			}

			if ( isOccupied( destinationSquare ) ) {
				disallowedMoves.add( potentialMove );
			}

			Side side = getSide( square );
			int intermediateRank = getPawnDoubleMoveIntermediateRank( side );
			if ( rankOfSquare( destinationSquare ) == getDoubleMoveRank( side ) &&
				rankOfSquare( square ) == getPawnInitialRank( side )
				&& isOccupied( fileOfSquare( square ) + intermediateRank ) ) {

				disallowedMoves.add( potentialMove );
			}
		}
		return disallowedMoves;
	}

	Side getSide( String square ) {
		final Piece piece = pieces.get( square );
		if ( piece == null ) {
			//TODO: correct? Some code relied on this.
			return null;
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
		return ( getDoubleMoveRank( side ) + getPawnInitialRank( side ) ) /2;
	}

	/**
	 * Get rank staying on which a pawn can execute
	 * en passant capture
	 * if previous move from another side was a double pawn move
	 * @param side
	 * @return rank with en passant possibility
	 */
	static int getEnPassantPossibleRank( Side side ) {
		return getDoubleMoveRank( side.opposite() );
	}

	static int getPawnInitialRank( Side side ) {
		return PAWN_INITIAL_RANKS.get( side );
	}

	/**
	 * Get pawn rank that is reachable from current rank by SINGLE move
	 * @param pawnRank
	 * @param side pawn side
	 * @return pawn rank
	 */
	private static int getPawnNextRank( int pawnRank, Side side ) {
		VerticalDirection direction = getPawnMovementDirection( side );
		return Board.rankTo( pawnRank, direction );
	}

	private static VerticalDirection getPawnMovementDirection( Side side ) {
		return side == Side.WHITE ?
		VerticalDirection.UP :
		VerticalDirection.DOWN;
	}

	private static int getPawnPreviousRank( int pawnRank, Side side ) {
		VerticalDirection direction =
			getPawnMovementDirection( side ).opposite();

		return Board.rankTo( pawnRank, direction );
	}

	/**
	 * @param side
	 * @return rank from which next pawn move can reach promotion rank
	 */
	private static int getRankBeforePromotion( Side side ) {
		return getPawnPreviousRank( getPromotionRank( side ), side );
	}

	private static int getPromotionRank( Side side ) {
		return PAWN_PROMOTION_RANKS.get( side );
	}

	/**
	 * Add to the result set all possible cases of promoting a pawn
	 * of given side in the file provided
	 * @param result result set to be modified
	 * @param file file place of promotion
	 * @param side side of pawn
	 */
	private static void addPromotionResult( Set<String> result, String file, Side side ) {
		for ( PieceType pieceToPromote : PIECES_TO_PROMOTE_FROM_PAWN ) {
			result.add( file + getPromotionRank( side ) + pieceToPromote.getNotation() );
		}
	}

	/**
	 * Check if square provided is occupied by the side
	 * @param square square to be validated
	 * @param side side which is desired to be
	 * @return true if square is occupied by the side, false otherwise
	 * (means NOT occupied or occupied by the opposite side)
	 */
	private boolean isOccupiedBy( String square, Side side ) {
		//if not found is null -> null != side
		final Piece piece = pieces.get( square );
		return piece != null && piece.getSide() == side;
	}

	private boolean isOccupied( String square ) {
		return !isEmptySquare( square );
	}

	static int getDoubleMoveRank( Side side ) {
		return getPawnNextRank( getPawnNextRank( getPawnInitialRank( side ), side ), side );
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
		return new PositionGenerator( this ).generate( squareFrom, move );
	}

	PieceType getPieceType( String squareFrom ) {
		return pieces.get( squareFrom ).getPieceType();
	}

	void copyPiecesInto( Position position ) {
		//cloning position
		for ( String square : pieces.keySet() ) {
			//looks safe as both keys and pieces are IMMUTABLE
			position.pieces.put( square, pieces.get( square ) );
		}
	}

	/**
	 * Check if square is empty (not occupied by any piece)
	 * @param square square to check
	 * @return true if square is empty
	 */
	boolean isEmptySquare( String square ) {
		return getSide( square ) == null;
	}

	/**
	 * If previous move was done by pawn (double-step from initial position)
	 * returns the file of movement, otherwise null
	 * @return possible en passant file if double-move done
	 */
	String getPossibleEnPassantFile() {
		return this.enPassantFile;
	}

	void addPawn( Side side, String square ) {
		add( side, square, PieceType.PAWN );
	}

	void addQueen( Side side, String square ) {
		add( side, square, PieceType.QUEEN );
	}

	void add( Side side, String square, PieceType pieceType ) {
		if ( !isSquareValid( square ) ) {
			throw new IllegalArgumentException( "Wrong destination square: " + square );
		}

		pieces.put( square, new Piece( pieceType, side ) );
	}

	boolean hasQueen( Side side, String square ) {
		return hasPiece( side, square, PieceType.QUEEN );
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
		return hasPiece( side, square, PieceType.PAWN );
	}

	boolean hasPiece( Side side, String square, PieceType pieceType ) {
		final Piece piece = pieces.get( square );
		return piece != null &&
				piece.getPieceType() == pieceType &&
				side == piece.getSide();
	}

	Piece getPiece( String square ) {
		return pieces.get( square );
	}

	//currently for tests only...
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for( String square : pieces.keySet() ) {
			builder.append( pieces.get( square ) );
		}
		return builder.toString();
	}

	void removePiece( String square ) {
		pieces.remove( square );
	}
}
