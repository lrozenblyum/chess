package com.leokom.chess.engine;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.leokom.chess.engine.Board.*;
import static com.leokom.chess.engine.InitialPosition.getPawnInitialRank;
import static com.leokom.chess.utils.CollectionUtils.filterMapByValues;
import static java.util.stream.Collectors.toSet;

/**
 * Current position on-board
 * It contains all game state that's important
 * to take decisions about possible moves.
 *
 * Thus it contains historical data like
 * <ul>
 *     <li>Side that can execute a move right now</li>
 *     <li>Has king moved?</li>
 *     <li>Has rook moved?</li>
 * </ul>
 *
 * The position SHOULD be immutable. This is a strict desired state
 * of the class.
 * Mutators that exist here MUST be moved to some 'position builder'
 *
 *
 * Author: Leonid
 * Date-time: 21.08.12 15:55
 */
public class Position implements GameState< Move > {
	/**
	 * Chess rules mention moves counter must be calculated
	 * for both players
	 */
	private static final int PLIES_IN_MOVE = 2;

	//by specification - the furthest from starting position
	//(in theory it means possibility to extend for fields others than 8*8)
	private static final int WHITE_PAWN_PROMOTION_RANK = MAXIMAL_RANK;
	private static final int BLACK_PAWN_PROMOTION_RANK = MINIMAL_RANK;

	private static final Map< Side, Integer > PAWN_PROMOTION_RANKS = 
			ImmutableMap.of( Side.WHITE, WHITE_PAWN_PROMOTION_RANK, Side.BLACK, BLACK_PAWN_PROMOTION_RANK );

	private static final Set< PieceType > PIECES_TO_PROMOTE_FROM_PAWN =
		ImmutableSet.of( PieceType.QUEEN, PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP );


	//all pieces currently present on the board
	private final Map< String, Piece > pieces = new HashMap<>();

	//historical state of the Game:

	//permanent state (which cannot change till the end of game

	//experiment: instead of exposing it via constructor,
	//(constructor is annoying when we don't need the field)
	//expose package-private setter

	//explicitly setting initial state for clarity
	private Set< Side > hasKingMoved = new HashSet<>();

	private Set< Side > hasARookMoved = new HashSet<>();

	private Set< Side > hasHRookMoved = new HashSet<>();

	private Side sideToMove;

	private Result gameResult;
	private boolean terminal;
	private Side winningSide;


	private boolean waitingForAcceptDraw;
	private Rules rules;

    void setHasKingMoved( Side side ) {
		this.hasKingMoved.add( side );
	}

	void setHasARookMoved( Side side ) {
		this.hasARookMoved.add( side );
	}

	void setHasHRookMoved( Side side ) {
		this.hasHRookMoved.add( side );
	}

	void setEnPassantFile( Character enPassantFile ) {	this.enPassantFile = enPassantFile; }

	//temporary state in game (which could change)
	private Character enPassantFile;

	//ply is the smallest movement in chess
	//a move consists of 2 plies
	//https://chessprogramming.wikispaces.com/Ply
	private int pliesCount;

	//the ply after which we should calculate 75 moves to
	//make obligatory draw (in case no pawn moves or captures are done)
	//it's also	 used to search for 50 moves rule claim draw possibility
    private int plyNumberToStartNoPawnMovementNoCaptureCalculation;

	/**
	 * Create position.
	 *
	 * By default king's right to castle is NOT lost (king and rooks
	 * are considered to be not have moved before) which might be inconsistent with actual position
	 *
	 * By default en passant file is absent
	 *
	 * @param sideToMove side which turn will be now, null for terminal positions
	 *
	 */
	public Position( Side sideToMove ) {
		this.sideToMove = sideToMove;
		this.rules = Rules.DEFAULT;
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
	 *               PRE-CONDITION: there is some piece on the square.
	 * @return not-null set of available moves from square (could be empty for sure)
	 *
	 * Move is now interpreted as following:
	 * 1) square's canonical representation if it's univocal (e.g. any pawn move including capture except promotion)
	 * While capture is usually indicated as x, from POV of single position it doesn't matter -
	 * the destination field correctly determines the result position in this case
	 * 2) square's canonical representation + upper-case of promoted piece from pawn (e.g. a8N -
	 * if we promoted to Knight)
	 *
	 */
	Set<String> getMovesFrom( String square ) {
		final Set<String> potentialMoves = getPotentialMoves( square );

		Set< String > result = new HashSet<>( potentialMoves );

		//3.1 It is not permitted to move a piece to a square occupied by a piece of the same colour.

		//looks rather safe to ignore the fact that result set can contain 'e8Q' (pawn promotions)
		//all pawn-specific clean-up is done on pawn level
		result.removeIf( move -> isOccupiedBy( move, getSide( square ) ) );

		// 3.9 'No piece can be moved that will ... expose the king of the same colour to check
		//... or leave that king in check' is also covered here.
		//castling is also covered here
		result.removeIf( move -> this.move( new Move( square, move ) ).isKingInCheck( getSide( square ) ) );

		//1.2 ’capturing’ the opponent’s king ... not allowed

		//no need to filter explicitly by opponent's king
		//anyway we couldn't move to a square occupied by OUR king
		result.removeIf( move -> this.isOccupiedBy( Move.getDestinationSquare( move ), PieceType.KING ) );

		return result;
	}

	//artificial method born due to need to exclude
	//some moves from pool of the 'potential moves'
	//due to king check conditions and 'cannot move to occupied by our side square'
	//returns potentially Immutable set
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
		if ( hasKingMoved.contains( side ) ) {
			return result;
		}

		//cannot castle if under check
		if ( isKingInCheck( side ) ) {
			return result;
		}

		int castlingRank = InitialPosition.getNotPawnInitialRank( side );

		//TODO: this condition as also covered by hasKingMoved
		//but we need to keep that flag in synchronous-state
		final char kingFile = 'e';
		if ( square.equals( square( kingFile, castlingRank ) ) ) {
			//TODO: the first condition is excessive - second covers it
			final char kingSideRookFile = 'h';
			if ( isOccupiedBy( square( kingSideRookFile, castlingRank ), side ) &&
				!hasHRookMoved.contains( side ) &&
				!isSquareAttacked( side, square( 'f', castlingRank ) ) &&
				isFreeRankBetween( kingFile, kingSideRookFile, castlingRank )) {
				result.add( square( 'g', castlingRank ) );
			}

			final char queenSideRookFile = 'a';
			if ( isOccupiedBy( square( queenSideRookFile, castlingRank ), side ) &&
				!hasARookMoved.contains( side ) &&
				!isSquareAttacked( side, square( 'd', castlingRank ) ) &&
				isFreeRankBetween( queenSideRookFile, kingFile, castlingRank )) {
				result.add( square( 'c', castlingRank ) );
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
	private boolean isFreeRankBetween( char leftFile, char rightFile, int rank ) {
		return getSquaresBetween( leftFile, rightFile, rank )
			.allMatch( this::isFree );
	}

	private Set< String > getSquaresAttackedFromSquare( String square ) {
		//assuming square is occupied...
		switch ( pieces.get( square ).getPieceType() ) {
			case PAWN:
				return getSquaresAttackedByPawn( square ).collect( Collectors.toSet() );
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
				throw new IllegalArgumentException( "Piece type is invalid: " + pieces.get( square ).getPieceType() );
		}
	}

	private Set<String> getSquaresAttackedByKing( String square ) {
		//TODO: this is similar to Queen (in part of all directions)
		//could we reuse queen's movement?
		Set< String > result = new HashSet<>();

		//diagonally
		for ( HorizontalDirection horizontalDirection : HorizontalDirection.all() ) {
			for ( VerticalDirection verticalDirection : VerticalDirection.all() ) {
				squareDiagonally( square, horizontalDirection, verticalDirection ).
				ifPresent( result::add );
			}
		}

		//left/right/top/bottom
		for ( Direction direction : Direction.all() ) {
			squareTo( square, direction )
			.ifPresent( result::add );
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

		for ( Direction direction : Direction.all() ) {
			Optional< String > runningSquare = Optional.of( square );

			do {
				runningSquare = squareTo( runningSquare.get(), direction );
				runningSquare.ifPresent( result::add );
			} while ( runningSquare.isPresent() && isFree( runningSquare.get() ) );
		}

		return result;
	}

	public Set<String> getSquaresOccupiedBySide( Side neededSide ) {
		return getSquaresOccupiedBySideToStream( neededSide ).collect( toSet() );
	}

	private Stream<String> getSquaresOccupiedBySideToStream( Side neededSide ) {
		return pieces.keySet().stream().filter( square -> this.isOccupiedBy( square, neededSide ) );
	}

	private boolean isKingInCheck( Side side ) {
		return kingStream( side )
				.anyMatch( kingSquare -> isSquareAttacked( side, kingSquare ) );
	}

	//we specify OUR side here because square might be empty
	//so we might check only potential attack
	private boolean isSquareAttacked( Side side, String square ) {
		return getSquaresAttackedByToStream( side.opposite() ).anyMatch( square::equals );
	}

	//get stream of squares with king of a given side is located
	//the stream can contain 1 item in usual chess and 0 in our tests
	private Stream< String > kingStream( Side side ) {
		return filterMapByValues( pieces, new Piece( PieceType.KING, side )::equals )
        .map( Map.Entry::getKey );
	}

	/**
	 * Get squares that are controlled (attacked) by the given side
	 * Both convenient for different Players implementations
	 * and also used internally
	 * @param side side of our interest
	 * @return set of all squares occupied by the given side
	 */
	public Set< String > getSquaresAttackedBy( Side side ) {
		return getSquaresAttackedByToStream( side ).collect( Collectors.toSet() );
	}

	private Stream< String > getSquaresAttackedByToStream( Side side ) {
		return getSquaresOccupiedBySideToStream( side )
				.map( this::getSquaresAttackedFromSquare )
				.flatMap( Collection::stream );
	}

	/**
	 * The method is generic and allows to know who is attacking a square
	 * (important both for attackers and protectors)
	 * @param attackerSide side of interest
	 * @param targetSquare square of interest
	 * @return stream of squares from which attackerSide has control over targetSquare
	 */
	public Stream< String > getSquaresAttackingSquare( Side attackerSide, String targetSquare ) {
		return
			getSquaresOccupiedBySideToStream( attackerSide )
			.filter( square -> getSquaresAttackedFromSquare( square ).contains( targetSquare ) );
	}

	//this method can be formed either as:
	//rook moves+bishop moves
	//or ( rook attacked + bishop attacked ) - (busy by our pieces)
	private Set<String> getQueenMoves( String square ) {
		//this works in assumption that rook's castling is NOT included into
		//getRookMoves. Castling is considered as King's move
		//however due to current notation for castling it's not harmful
		//(we're not using 0-0 yet)

		return Sets.union( getSquaresAttackedByRook( square ), getSquaresAttackedByBishop( square ) );
	}


	private Set<String> getSquaresAttackedByBishop( String square ) {
		Set< String > result = new HashSet<>();

		for ( HorizontalDirection horizontalDirection : HorizontalDirection.all() ) {
			for ( VerticalDirection verticalDirection : VerticalDirection.all() ) {
				Optional< String > movingSquare = Optional.of( square );
				do {
					movingSquare = squareDiagonally( movingSquare.get(), horizontalDirection, verticalDirection );
					movingSquare.ifPresent( result::add );
				}
				//the loop will include first busy square on its way to the result
				while ( movingSquare.isPresent() && isFree( movingSquare.get() ) );
			}
		}
		return result;
	}

	//NOTE: from point of view of en passant we
	//still have the square diagonally-front attacked
	private Stream< String > getSquaresAttackedByPawn( String square ) {
		return HorizontalDirection.all().stream()
			.map( horizontalDirection -> {
				final Side side = getSide( square );

				return squareDiagonally( square, horizontalDirection, getPawnMovementDirection( side ) );
			} ).filter( Optional::isPresent ).map( Optional::get );
	}

	private Set<String> getPawnMoves( String square ) {
		final Set<String> result = new HashSet<>();

		final char file = fileOfSquare( square );
		final int rank = rankOfSquare( square );

		//NOTE: the possible NULL corresponds to to-do in javadoc
		final Side side = getSide( square );

		if ( rank == getRankBeforePromotion( side ) ) {
			getPromotionResult( file, side )
				.forEach( result::add );

			getSquaresAttackedByPawn( square )
				.filter( attackedSquare -> canBeAttackedUsually( side, attackedSquare ) )
				.flatMap( attackedSquare ->
					getPromotionResult( fileOfSquare( attackedSquare ), side )
				)
				.forEach( result::add );
		}
		else {
			result.add( square( file, getPawnNextRank( rank, side ) ) );
			if ( rank == getPawnInitialRank( side ) ) {
				result.add( square( file, getDoubleMoveRank( side ) ) );
			}

			getSquaresAttackedByPawn( square )
			.filter( attackedSquare ->
					canBeAttackedUsually( side, attackedSquare ) ||
					canEnPassant( side, attackedSquare )  )
			.forEach( result::add );
		}

		result.removeIf( move -> !canPawnMove( square, move ) );
		return result;
	}

	//3.7 d. A pawn attacking a square crossed by an opponent’s pawn which has advanced two squares
	// in one move from its original square may capture this opponent’s pawn
	// as though the latter had been moved only one square
	private boolean canEnPassant( Side side, String attackedSquare ) {
		return enPassantFile != null &&
			attackedSquare.equals( square( enPassantFile, getPawnDoubleMoveIntermediateRank( side.opposite() ) ) );
	}

	private boolean canBeAttackedUsually( Side side, String attackedSquare ) {
		return isOccupiedBy( attackedSquare, side.opposite() );
	}

	private Set<String> getSquaresAttackedByKnight( String square ) {
		//shifts pairs: horizontal and vertical shift
		//they will be combined with all possible vertical/horizontal directions
		int [][] shifts = { {1, 2}, {2, 1} };

		Set< String > knightMoves = new HashSet<>();
		for ( int [] shiftPair : shifts ) {
			for ( HorizontalDirection horizontalDirection : HorizontalDirection.all() ) {
				for ( VerticalDirection verticalDirection : VerticalDirection.all() ) {
					Board.squareTo( square, horizontalDirection, shiftPair[ 0 ],
							verticalDirection, shiftPair[ 1 ] )
					.ifPresent( knightMoves::add );
				}
			}
		}

		return knightMoves;
	}

	private boolean canPawnMove( String square, String potentialMove ) {
		String destinationSquare = Move.getDestinationSquare( potentialMove );

		//if file is different - it's capture and should be allowed
		return !sameFile( destinationSquare, square ) ||
				// here we deny captures if a pawn moved forward
				//pawn cannot move FORWARD to occupied square
				( !isOccupied( destinationSquare ) &&
				  !isPawnDoubleMoveProhibited( square, destinationSquare ) );
	}

	private boolean isPawnDoubleMoveProhibited( String square, String destinationSquare ) {
		Side side = getSide( square );
		int intermediateRank = getPawnDoubleMoveIntermediateRank( side );
		return rankOfSquare( destinationSquare ) == getDoubleMoveRank( side ) &&
				rankOfSquare( square ) == getPawnInitialRank( side )
				&& isOccupied( square( fileOfSquare( square ), intermediateRank ) );
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
	 * @param side pawn side
	 * @return rank with en passant possibility
	 */
	static int getEnPassantPossibleRank( Side side ) {
		return getDoubleMoveRank( side.opposite() );
	}

	/**
	 * Get pawn rank that is reachable from current rank by SINGLE move
	 * @param pawnRank pawn rank
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
	 * @param side side of player
	 * @return rank from which next pawn move can reach promotion rank
	 */
	private static int getRankBeforePromotion( Side side ) {
		return getPawnPreviousRank( getPromotionRank( side ), side );
	}

	private static int getPromotionRank( Side side ) {
		return PAWN_PROMOTION_RANKS.get( side );
	}

	/**
	 * Get all possible cases of promoting a pawn
	 * of given side in the file provided
	 * @param file file place of promotion
	 * @param side side of pawn
	 * @return stream of possible promotions
	 */
	private static Stream< String > getPromotionResult( char file, Side side ) {
		return PIECES_TO_PROMOTE_FROM_PAWN.stream().map(
			pieceToPromote -> square( file, getPromotionRank( side ) ) + pieceToPromote.getNotation()
		);
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
		return !isFree( square );
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
		return pieces.get( squareFrom ) != null ? pieces.get( squareFrom ).getPieceType() : null;
	}

	public boolean hasARookMoved( Side side ) {
		return hasARookMoved.contains( side );
	}

	public boolean hasHRookMoved( Side side ) {
		return hasHRookMoved.contains( side );
	}

	public boolean hasKingMoved( Side side ) {
		return hasKingMoved.contains( side );
	}

	/**
	 * Copy pieces from current position to the destination
	 * Copy state (like info if the king has moved)
	 * @param position destination position
	 */
	void copyStateTo( Position position ) {
		//cloning position
		for ( String square : pieces.keySet() ) {
			//looks safe as both keys and pieces are IMMUTABLE
			position.pieces.put( square, pieces.get( square ) );
		}

		//cloning the state!
		//Side is immutable, thus no extra safety measures are needed
		position.hasKingMoved = new HashSet<>( this.hasKingMoved );
		position.hasARookMoved = new HashSet<>( this.hasARookMoved );
		position.hasHRookMoved = new HashSet<>( this.hasHRookMoved );

		//little overhead but ensuring we really copy the FULL state
		position.waitingForAcceptDraw = this.waitingForAcceptDraw;

		position.rules = this.rules;
		position.pliesCount = this.pliesCount;
		position.plyNumberToStartNoPawnMovementNoCaptureCalculation = this.plyNumberToStartNoPawnMovementNoCaptureCalculation;

		position.terminal = this.terminal;
	}

	/**
	 * Create 'mirror' position.
	 * Since Position now encapsulates full game state
	 * (including side to move)
	 * this is the way to create the same position with a single difference
	 * side to move is opposite
	 *
	 * @return mirror position
	 */
	public Position toMirror() {
		Position position = new Position( this.sideToMove.opposite() );
		copyStateTo( position );
		return position;
	}

	/**
	 * Check if a given square is free (not occupied by any piece)
	 * @param square square to check
	 * @return true if square is empty
	 */
	boolean isFree( String square ) {
		return pieces.get( square ) == null;
	}

	/**
	 * If previous move was done by pawn (double-step from initial position)
	 * returns the file of movement, otherwise null
	 * @return possible en passant file if double-move done
	 */
	Character getPossibleEnPassantFile() {
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
	 * @param side pawn side
	 * @param square square to find the pawn
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
		return pieces.entrySet().stream()
			.map( entry -> entry.getKey() + ":" + entry.getValue() )
			.collect( Collectors.joining( " " ) );
	}

	void removePiece( String square ) {
		pieces.remove( square );
	}

	/**
	 * Mechanically move piece unconditionally (non-validating)
	 * from from to to
	 */
	void moveUnconditionally( String from, String to ) {
		Move move = new Move( from, to );
		Piece piece = getPiece( from );
		removePiece( from );

		if ( move.isPromotion() ) {
			 add( piece.getSide(), move.getDestinationSquare(), move.getPromotionPieceType() );
		}
		else {
			add( piece.getSide(), move.getDestinationSquare(), piece.getPieceType() );
		}
	}

	/**
	 * Get set of moves possible from the position.
	 * Since it encapsulates side - the moves are collected
	 * for #getSideToMove()
	 * @return set of possible legal moves
	 */
	public Set< Move > getMoves() {
		if ( terminal ) {
			return new HashSet<>();
		}

		final Set<Move> result = getSquaresOccupiedBySideToStream( sideToMove )
			.flatMap( square -> getMovesFrom( square ).stream().map( move -> new Move( square, move ) ) )
			.collect( Collectors.toSet() );

		//resign is possible if there is at least one other move
		//correct?
		if ( !result.isEmpty() ) {

			//obligatory draw must be checked AFTER moves detection
			//to distinguish checkmate at 150 ply case!
			if ( isObligatoryDraw() ) {
				markDraw();
				this.gameResult = Result.DRAW_BY_OBLIGATORY_MOVES;
				return new HashSet<>();
			}

			if ( canClaimDraw() ) {
				result.add( Move.CLAIM_DRAW );
			}

			result.add( Move.OFFER_DRAW );
			result.add( Move.RESIGN );
			if ( waitingForAcceptDraw ) {
				result.add( Move.ACCEPT_DRAW );
			}
		} else if ( !isKingInCheck( sideToMove ) ) {
			this.gameResult = Result.STALEMATE;
			markDraw();
		}

		return result;
	}

	private void markDraw() {
		//TODO: position mutability due to flaws in design

		//marking it also terminal to avoid next checks for Moves
		this.terminal = true;
		//must be done
		this.sideToMove = null;
		//for clarity
		this.winningSide = null;
	}

	private boolean isObligatoryDraw() {
		final OptionalInt movesTillDraw = rules.getMovesTillDraw();
		return movesTillDraw.isPresent() &&	enoughMovesWithoutPawnMovementAndCapture(movesTillDraw.getAsInt());
	}

	private boolean canClaimDraw() {
		return enoughMovesWithoutPawnMovementAndCapture( rules.getMovesTillClaimDraw() );
	}

	private boolean enoughMovesWithoutPawnMovementAndCapture( int movesToBeEnough ) {
		return ( pliesCount - plyNumberToStartNoPawnMovementNoCaptureCalculation)
				>= movesToBeEnough * PLIES_IN_MOVE;
	}

	/**
	 * Check whether the given move is legal in current position
	 * @param move move to check
	 * @return true if the move is legal, false otherwise
	 */
	public boolean isLegal( Move move ) {
		//NOTE: great point to OPTIMIZE, we don't need calculating the whole set
		//lazy calculation via lambdas could greatly improve performance
		return getMoves().contains( move );
	}

	/**
	 * @return legal non-special moves
	 */
	Set< Move > getNormalMoves() {
		return getMoves().stream().filter( move -> !move.isSpecial() ).collect( toSet() );
	}

	public Stream< Piece > getPieces( Side side ) {
		return pieces.values().stream().filter( piece -> piece.getSide() == side );
	}

	public Side getSideToMove() {
		return sideToMove;
	}

	void setSideToMove( Side sideToMove ) {
		this.sideToMove = sideToMove;
	}

	/**
	 * Check if position is terminal
	 * (final, meaning end of game).
	 * No more moves are legal from a terminal position
	 *
	 * @return true if position is terminal
	 */
	public boolean isTerminal() {
		return getMoves().isEmpty();
	}

	/**
	 * Get game result
	 * @return the game result
	 * @throws IllegalStateException if game is not over yet
	 */
	public Result getGameResult() {
		validateGameIsOver();

		return gameResult;
	}

	/**
	 * Get side that has won the game
	 *
	 * @return side that has won the game
	 *
	 * @throws java.lang.IllegalStateException when game is not finished yet
	 */
	public Side getWinningSide() {
		validateGameIsOver();

		//winningSide != null is currently only after resign
		//winningSide == null && sideToMove != null currently after checkmate (due to our lazy nature of detection of checkmate)
		//first try to make that calculation not-lazy failed, with StackOverflow
		//it tried to create more and more positions in predicate that checks whether we expose our king to check
		//winningSide == null && sideToMove == null currently after draw
		//simulated the same behaviour for case when draw achieved due to 75 moves rule
		return winningSide != null ? winningSide : sideToMove != null ?  sideToMove.opposite() : null;
	}

	private void validateGameIsOver() {
		if ( !isTerminal() ) {
			throw new IllegalStateException( "Game has not yet finished. Valid moves: " + getMoves() );
		}
	}

	/**
	 * Mark position as 'terminal'
	 * @param winningSide side that has won the game
	 */
	void setTerminal( Side winningSide ) {
		this.terminal = true;
		this.winningSide = winningSide;
	}

	void setWaitingForAcceptDraw( boolean waitingForAcceptDraw ) {
		this.waitingForAcceptDraw = waitingForAcceptDraw;
	}

	public static Position getInitialPosition( Rules rules ) {
		return InitialPosition.generate( rules );
	}

	public Rules getRules() {
		return this.rules;
	}

	void setRules( Rules rules ) {
		this.rules = rules;
	}

	void incPliesCount() {
		++pliesCount;
	}

	void restartObligatoryDrawCounter() {
	    this.plyNumberToStartNoPawnMovementNoCaptureCalculation = pliesCount;
	}

	/**
	 * Detect whether a move executed from the position would be a capture
	 * @param move potential move to execute
	 * @return true if the move is capture
	 */
	boolean isCapture( Move move ) {
		return canBeAttackedUsually( getSideToMove(), move.getDestinationSquare() ) ||
				isEnPassant( move );
	}

	//position generator also knows about en passant
	//maybe need generalizing

	//this is NON-VALIDATING checker
	private boolean isEnPassant( Move move ) {
		return isCaptureByPawn( move ) && isFree( move.getDestinationSquare() );
	}

	//capture by pawn is done diagonally - the file is changed
	private boolean isCaptureByPawn( Move move ) {
		return getPieceType( move.getFrom() ) == PieceType.PAWN &&
				!Board.sameFile( move.getFrom(), move.getDestinationSquare() );
	}

	/**
	 * Usually moves are executed by a person to move.
	 * However some special moves can be executed even when it's not your turn
	 * @return set of moves that can be executed by the opponent
	 */
	public Set<Move> getMovesForOpponent() {
		Set< Move > moves = new HashSet<>();
		//resign can be done at any moment
		moves.add( Move.RESIGN );
		if ( this.waitingForAcceptDraw ) {
			moves.add( Move.ACCEPT_DRAW );
		}
		return moves;
	}

	/**
	 * Get human-understandable move number.
	 *
	 * e.g. in the game
	 * 1. e2-e4 e7-e5
	 * 2. f2-f4
	 *
	 * e2-e4 &rarr; 1
	 * e7-e5 &rarr; 1
	 * f2-f4 &rarr; 2
	 *
	 * @return move number
	 */
	public int getMoveNumber() {
		return pliesCount / PLIES_IN_MOVE + 1;
	}

	void setGameResult(Result gameResult) {
		this.gameResult = gameResult;
	}

}
