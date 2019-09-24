package com.leokom.games.chess.engine;

import java.util.Arrays;

/**
 * Define all possible piece types for chess
 * Author: Leonid
 * Date-time: 05.09.12 21:36
 */
public enum PieceType {
	/**
	 * The piece that initially looks weak but
	 * in team with other pawns and pieces could be very powerful
	 * It also could be promoted to other pieces except King
	 */
	PAWN( "" ),

	/**
	 * 3.6. The knight may move to one of the squares nearest to that on which it stands but not on the same rank, file or diagonal.
	 */
	KNIGHT ( "N" ),

	/**
	 * 3.2. The bishop may move to any square along a diagonal on which it stands.
	 */
	//NOTE: this name MUSTN'T be confused with anything religious.
	// It's just a common name for the piece which e.g. in Russian has name слон ('elephant')
	BISHOP( "B" ),

	/**
	 * 3.3 The rook may move to any square along the file or the rank on which it stands
	 * See also king description (when available)
	 * for castling
	 */
	ROOK( "R" ),

	/**
	 * 3.4 The queen may move to any square along the file, the rank or a diagonal on which it stands.
	 *
	 * The piece that is considered to be the strongest in most cases.
	 * However the game may continue without it (in contrary to game without king)
	 */
	QUEEN ( "Q" ),

	/**
	 * The most important piece in the game.
	 *
	 * 5.1. a) The game is won by the player who has checkmated his opponent’s king
	 */
	KING( "K" );

	private final String notation;

	/**
	 * Create piece type with internal standardized string notation
	 * @param notation string notation for the piece
	 */
	PieceType( String notation ) {
		this.notation = notation;
	}

	static PieceType byNotation( String notation ) {
		return Arrays.stream( values() )
			.filter( pieceType -> pieceType.notation.equals( notation ) )
			.findAny()
			.orElseThrow( () -> new IllegalArgumentException( "No piece type is known for notation: " + notation ) );
	}

	String getNotation() {
		return notation;
	}
}
