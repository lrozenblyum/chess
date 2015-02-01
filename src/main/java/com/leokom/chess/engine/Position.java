package com.leokom.chess.engine;

import com.leokom.chess.utils.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.leokom.chess.engine.Board.*;
import static com.leokom.chess.engine.InitialPosition.getPawnInitialRank;
import static com.leokom.chess.utils.CollectionUtils.addIfNotNull;
import static java.util.stream.Collectors.toSet;

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

	//by specification - the furthest from starting position
	//(in theory it means possibility to extend for fields others than 8*8)
	private static final int WHITE_PAWN_PROMOTION_RANK = MAXIMAL_RANK;
	private static final int BLACK_PAWN_PROMOTION_RANK = MINIMAL_RANK;

	//thread-safe for read usage, should we use some 'immutable map'?
	private static final Map< Side, Integer > PAWN_PROMOTION_RANKS = new HashMap<Side, Integer>() { {
		put( Side.WHITE, WHITE_PAWN_PROMOTION_RANK );
		put( Side.BLACK, BLACK_PAWN_PROMOTION_RANK );
	}};

	//thread safe
	private static final Set< PieceType > PIECES_TO_PROMOTE_FROM_PAWN =
		Collections.unmodifiableSet( EnumSet.of(
				PieceType.QUEEN, PieceType.ROOK,
				PieceType.KNIGHT, PieceType.BISHOP ) );


	//all pieces currently present on the board
	private final Map< String, Piece > pieces = new HashMap<>();

	//historical state of the Game:

	//permanent state (which cannot change till the end of game

	//experiment: instead of exposing it via constructor,
	//(constructor is annoying when we don't need the field)
	//expose package-private setter

	//explicitly setting initial state for clarity
	private Map< Side, Boolean > hasKingMoved = new HashMap< Side, Boolean >() {{
		put( Side.WHITE, false );
		put( Side.BLACK, false );
	}};

	private Map< Side, Boolean > hasARookMoved = new HashMap< Side, Boolean >() {{
		put( Side.WHITE, false );
		put( Side.BLACK, false );
	}};

	private Map< Side, Boolean > hasHRookMoved = new HashMap< Side, Boolean >() {{
		put( Side.WHITE, false );
		put( Side.BLACK, false );
	}};

	void setHasKingMoved( Side side ) {
		this.hasKingMoved.put( side, true );
	}

	void setHasARookMoved( Side side ) {
		this.hasARookMoved.put( side, true );
	}

	void setHasHRookMoved( Side side ) {
		this.hasHRookMoved.put( side, true );
	}

	void setEnPassantFile( String enPassantFile ) {	this.enPassantFile = enPassantFile; }

	//temporary state in game (which could change)
	private String enPassantFile;

	//TODO: in theory the flag could be inconsistent with actual position...
	//maybe need some builder?

	/**
	 * Create position.
	 *
	 * By default king's right to castle is NOT lost (king and rooks
	 * are considered to be not have moved before) which might be inconsistent with actual position
	 *
	 * By default en passant file is absent
	 */
	public Position() {
	}

	private static final int VALID_SQUARE_LENGTH = 2;

	/**
	 * Get instance of initial chess position
	 * @return initial chess position
	 */
	public static Position getInitialPosition() {
		return InitialPosition.generate();
	}

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
		final Set<String> potentialMoves = getPotentialMoves( square );

		//3.1 It is not permitted to move a piece to a square occupied by a piece of the same colour.
		potentialMoves.removeAll( getSquaresOccupiedBySide( getSide( square ) ) );

		potentialMoves.removeAll( getSquaresThatExposeOurKingToCheck( square, potentialMoves ) );

		//1.2 ’capturing’ the opponent’s king ... not allowed
		potentialMoves.removeAll( getCapturesOfKing( potentialMoves ) );

		return potentialMoves;
	}

	private Collection< String > getCapturesOfKing( Set< String > potentialMoves ) {
		//no need to filter explicitly by opponent's king
		//anyway we couldn't move to a square occupied by OUR king
		return potentialMoves.stream()
			.filter( move ->
				isOccupiedBy( Move.getDestinationSquare( move ), PieceType.KING )
			).collect( Collectors.toList() );
	}

	//artificial method born due to need to exclude
	//some moves from pool of the 'potential moves'
	//due to king check conditions and 'cannot move to occupied by our side square'
	private Set<String> getPotentialMoves( String square ) {
		switch ( getPieceType( square ) ) {
			case KNIGHT:
				return getSquaresAttackedByKnight( square );
			case BISHOP:
				return getSquaresAttackedByBishop( square );
			case ROOK:
				return getSquaresAttackedByRook( square );
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
		Set< String > result = getSquaresAttackedByKing( square );

		result.addAll( generatePossibleCastlingDestinations( square ) );

		//squares where king will be attacked WILL be removed in generic handler
		//to satisfy general rule : no move can expose king to check

		return result;
	}

	private Set<String> generatePossibleCastlingDestinations( String square ) {
		Set< String > result = new HashSet<>();

		Side side = getSide( square );

		//cannot castle if king has already moved
		if ( hasKingMoved.get( side ) ) {
			return result;
		}

		//cannot castle if under check
		if ( isKingInCheck( side ) ) {
			return result;
		}

		int castlingRank = InitialPosition.getNotPawnInitialRank( side );

		//TODO: this condition as also covered by hasKingMoved
		//but we need to keep that flag in synchronous-state
		final String kingFile = "e";
		if ( square.equals( kingFile + castlingRank ) ) {
			//TODO: the first condition is excessive - second covers it
			final String kingSideRookFile = "h";
			if ( isOccupiedBy( kingSideRookFile + castlingRank, side ) &&
				!hasHRookMoved.get( side ) &&
				!isSquareAttacked( side, "f" + castlingRank ) &&
				isFreeRankBetween( kingFile, kingSideRookFile, castlingRank )) {
				result.add( "g" + castlingRank );
			}

			final String queenSideRookFile = "a";
			if ( isOccupiedBy( queenSideRookFile + castlingRank, side ) &&
				!hasARookMoved.get( side ) &&
				!isSquareAttacked( side, "d" + castlingRank ) &&
				isFreeRankBetween( queenSideRookFile, kingFile, castlingRank )) {
				result.add( "c" + castlingRank );
			}
		}

		return result;
	}

	/**
	 * Check if all squares between leftFile and rankFile (exclusive) on the rank are free
	 * Return
	 * @param leftFile left outer border file
	 * @param rightFile right outer border file
	 * @param rank rank where to check squares
	 * @return true if all middle squares are free
	 */
	private boolean isFreeRankBetween( String leftFile, String rightFile, int rank ) {
		String leftBorderSquare = leftFile + rank;
		String rightBorderSquare = rightFile + rank;

		for ( String movingSquare = Board.squareTo( leftBorderSquare, HorizontalDirection.RIGHT );
			!movingSquare.equals( rightBorderSquare );
			movingSquare = Board.squareTo( movingSquare, HorizontalDirection.RIGHT ) ) {
			if ( isOccupied( movingSquare ) ) {
				return false;
			}
		}

		return true;
	}

	private Set< String > getSquaresAttackedFromSquare( String square ) {
		//assuming square is occupied...
		switch ( pieces.get( square ).getPieceType() ) {
			case PAWN:
				return getSquaresAttackedByPawn( square );
			case KNIGHT:
				return getSquaresAttackedByKnight( square );
			case BISHOP:
				return getSquaresAttackedByBishop( square );
			case ROOK:
				return getSquaresAttackedByRook( square );
			case QUEEN:
				return getSquaresAttackedByQueen( square );
			case KING:
				return getSquaresAttackedByKing( square );
		 	default:
				//TODO: unreachable code so far? How to improve?
				throw new IllegalArgumentException( "Piece type is invalid: " + pieces.get( square ).getPieceType() );
		}
	}

	private Set<String> getSquaresAttackedByKing( String square ) {
		//TODO: this is similar to Queen (in part of all directions)
		//could we reuse queen's movement?
		Set< String > result = new HashSet<>();

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

		return result;
	}

	private Set<String> getSquaresAttackedByQueen( String square ) {
		final Set<String> result = getSquaresAttackedByBishop( square );
		result.addAll( getSquaresAttackedByRook( square ) );
		return result;
	}

	private Set<String> getSquaresAttackedByRook( String square ) {
		Set< String > result = new HashSet<>();

		for ( Direction direction : Direction.values() ) {
			String runningSquare = square;

			do {
				runningSquare = squareTo( runningSquare, direction );
				addIfNotNull( result, runningSquare );
			} while ( runningSquare != null && isEmptySquare( runningSquare ) );
		}

		return result;
	}

	public Set<String> getSquaresOccupiedBySide( Side neededSide ) {
		return pieces.keySet().stream().filter( square -> pieces.get( square ).getSide() == neededSide ).collect( toSet() );
	}

	private Set<String> getSquaresThatExposeOurKingToCheck( String square, Set< String > potentialMoves ) {
		Set< String > result = new HashSet<>();

		//castling is also covered fine here
		for ( String move : potentialMoves ) {

			//works in assumption move is NON-validating
			final Position possiblePosition = this.move( new Move( square, move ) );
			if ( possiblePosition.isKingInCheck( getSide( square ) ) ) {
				result.add( move );
			}
		}

		return result;
	}

	//TODO: side must be part of Position, isn't it?
	private boolean isKingInCheck( Side side ) {

		final String kingSquare = findKing( side );
		//TODO: null is impossible in real chess, possible in our tests...
		return kingSquare != null && isSquareAttacked( side, kingSquare );
	}

	//we specify OUR side here because square might be empty
	//so we might check only potential attack
	private boolean isSquareAttacked( Side side, String square ) {
		return getSquaresAttackedBy( side.opposite() ).contains( square );
	}

	private String findKing( Side side ) {
		//TODO: null is impossible in real chess, possible in our tests...
		return pieces.entrySet().stream()
			.filter( ( entry ) ->
				entry.getValue().getPieceType() == PieceType.KING &&
				entry.getValue().getSide() == side
			)
			.map( Map.Entry::getKey ).findFirst().orElse( null );
	}

	/**
	 * Get squares that are controlled (attacked) by the given side
	 * Both convenient for different Players implementations
	 * and also used internally
	 * @param side side of our interest
	 * @return set of all squares occupied by the given side
	 */
	public Set< String > getSquaresAttackedBy( Side side ) {
		final Set< String > squaresOccupied = getSquaresOccupiedBySide( side );

		Set< String > result = new HashSet<>();

		for ( String squareOccupied : squaresOccupied ) {
			result.addAll( getSquaresAttackedFromSquare( squareOccupied ) );
		}
		return result;
	}

	//this method can be formed either as:
	//rook moves+bishop moves
	//or ( rook attacked + bishop attacked ) - (busy by our pieces)
	private Set<String> getQueenMoves( String square ) {
		//TODO: some Guava/CollectionUtils for simplification?

		//this works in assumption that rook's castling is NOT included into
		//getRookMoves. Castling is considered as King's move
		//however due to current notation for castling it's not harmful
		//(we're not using 0-0 yet)
		final Set< String > result = getSquaresAttackedByRook( square );
		result.addAll( getSquaresAttackedByBishop( square ) );
		return result;
	}


	private Set<String> getSquaresAttackedByBishop( String square ) {
		Set< String > result = new HashSet<>();

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
		Set< String > result = new HashSet<>();
		for ( HorizontalDirection horizontalDirection : HorizontalDirection.values() ) {
			final Side side = getSide( square );

			final String attackedSquare = squareDiagonally( square, horizontalDirection, getPawnMovementDirection( side ) );

			addIfNotNull( result, attackedSquare );
		}
		return result;
	}

	private Set<String> getPawnMoves( String square ) {
		final Set<String> result = new HashSet<>();

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
		int [][] shifts = { {1, 2}, {2, 1} };

		Set< String > knightMoves = new HashSet<>();
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

	/**
	 * Get set of moves from initial potentialPawnMoves
	 * that aren't allowed according to chess rules
	 * @param potentialPawnMoves moves that were detected as potential possibilities
	 * @param square current pawn position
	 * @return set of moves to be removed
	 */
	private Set<String> getImpossibleMovesForPawn( Set<String> potentialPawnMoves, String square ) {
		Set< String > disallowedMoves = new HashSet<>();
		for ( String potentialMove : potentialPawnMoves ) {
			String destinationSquare = Move.getDestinationSquare( potentialMove );

			//pawn cannot move to occupied square
			//if file is different - it's capture and should be allowed
			final boolean isMoveForward = sameFile( destinationSquare, square );
			if ( !isMoveForward ) {
				continue;
			}

			//probably it's already double-checked in common block of code
			//it's good to have it here since common block of code doesn't check
			//e8Q move correctness for moving to occupied plac
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

	/**
	 * Get side that occupies the given square
	 * @param square square to check
	 * @return side if the square is occupied, null if it's free
	 */
	public Side getSide( String square ) {
		final Piece piece = pieces.get( square );
		return ( piece == null ) ? null : piece.getSide();
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

	private boolean isOccupiedBy( String square, PieceType pieceType ) {
		final Piece piece = pieces.get( square );
		return piece != null && piece.getPieceType() == pieceType;
	}

	private boolean isOccupied( String square ) {
		return !isEmptySquare( square );
	}

	static int getDoubleMoveRank( Side side ) {
		return getPawnNextRank( getPawnNextRank( getPawnInitialRank( side ), side ), side );
	}

	/**
	 *
	 * use #move(Move)
	 */
	public Position move( String squareFrom, String move ) {
		return this.move( new Move( squareFrom, move ) );
	}

	/**
	 * Make move from squareFrom
	 * We guarantee returning a new position instead of
	 * modifying the current one.
	 *
	 * The implementation should execute the move provided, guaranteeing that unaffected
	 * pieces must be kept on the same place
	 * (NOTE: unaffected is not so easy as can be imagined, e.g. when we move en passant the piece we capture
	 * IS affected! )
	 * @param move act of movement
	 * @return new position, which is received from current by making 1 move
	 */
	public Position move( Move move ) {
		return new PositionGenerator( this ).generate( move );
	}

	public PieceType getPieceType( String squareFrom ) {
		return pieces.get( squareFrom ).getPieceType();
	}

	public boolean hasARookMoved( Side side ) {
		return hasARookMoved.get( side );
	}

	public boolean hasHRookMoved( Side side ) {
		return hasHRookMoved.get( side );
	}

	public boolean hasKingMoved( Side side ) {
		return hasKingMoved.get( side );
	}

	/**
	 * Copy pieces from current position to the destination
	 * Copy state (like info if the king has moved)
	 * @param position destination position
	 */
	void copyPiecesInto( Position position ) {
		//cloning position
		for ( String square : pieces.keySet() ) {
			//looks safe as both keys and pieces are IMMUTABLE
			position.pieces.put( square, pieces.get( square ) );
		}

		//cloning the state!
		for ( Side side : this.hasKingMoved.keySet() ) {
			position.hasKingMoved.put( side, this.hasKingMoved.get( side ) );
		}

		for ( Side side : this.hasARookMoved.keySet() ) {
			position.hasARookMoved.put( side, this.hasARookMoved.get( side ) );
		}

		for ( Side side : this.hasHRookMoved.keySet() ) {
			position.hasHRookMoved.put( side, this.hasHRookMoved.get( side ) );
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

	//TODO: temporary public
	//need better solution (like PositionBuilder?)
	//to avoid outside position change!!
	public void add( Side side, String square, PieceType pieceType ) {
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
			builder
				.append( square )
				.append ( ":" )
				.append( pieces.get( square ) );
		}
		return builder.toString();
	}

	void removePiece( String square ) {
		pieces.remove( square );
	}

	/**
	 * Mechanically move piece unconditionally (non-validating)
	 * from from to to
	 */
	void moveUnconditionally( String from, String to ) {
		Piece piece = getPiece( from );
		removePiece( from );
		add( piece.getSide(), to, piece.getPieceType() );
	}

	//TODO: probably moving side must be a part of the position itself
	//to be decided what's better representation for moves.
	public Set< Move > getMoves( Side side ) {
		final Set< Move > result = new HashSet<>();

		final Set<String> squares = getSquaresOccupiedBySide( side );
		for ( String square : squares ) {
			result.addAll( getMovesFrom( square ).stream().map( move -> new Move( square, move ) ).collect( toSet() ) );
		}

		return result;
	}

	public Stream< Piece > getPieces( Side side ) {
		return pieces.values().stream().filter( piece -> piece.getSide() == side );
	}
}
