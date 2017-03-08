package com.leokom.chess.player.legal.brain.internal.common;

import com.google.common.collect.ImmutableMap;
import com.leokom.chess.engine.PieceType;

import java.util.Map;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:28
 */
public final class MaterialValues {
	//heuristic, may be dynamic depending on situation on the board!
	public static final Map< PieceType,Integer > VALUES =
			ImmutableMap.< PieceType,Integer > builder()
			.put( PieceType.PAWN, 1 )
			.put( PieceType.KNIGHT, 3 )
			.put( PieceType.BISHOP, 3 )
			.put( PieceType.ROOK, 5 )
			.put( PieceType.QUEEN, 9 )
			//practically King is invaluable,
			//however for sum purposes like attackIndex
			// we need some value associated
			.put( PieceType.KING, 1000 ).build();

	private MaterialValues() {}
}
