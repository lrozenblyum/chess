package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;


final class ProtectionIndexCalculator {
	private ProtectionIndexCalculator() {}

	static float getIndex( Position targetPosition, Side ourSide ) {
		//checks level of protection
		return targetPosition.getSquaresOccupiedBySide(ourSide).stream().mapToLong(
				square -> targetPosition.getSquaresAttackingSquare( ourSide, square ).count()
		).sum();
	}
}
