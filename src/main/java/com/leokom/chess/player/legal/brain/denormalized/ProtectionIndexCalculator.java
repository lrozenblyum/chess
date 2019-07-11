package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;

import java.util.Set;


final class ProtectionIndexCalculator {
	private ProtectionIndexCalculator() {}

	static float getIndex(Position targetPosition, Side ourSide ) {
		Set<String> controlledByUs = targetPosition.getSquaresAttackedBy( ourSide );

		//TODO: very primitive: just checks what's protected and what's not
		return targetPosition.getSquaresOccupiedBySide( ourSide ).stream().mapToInt( square -> controlledByUs.contains( square ) ? 1 : 0 ).sum();
	}
}
