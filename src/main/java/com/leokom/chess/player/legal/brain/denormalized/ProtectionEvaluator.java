package com.leokom.chess.player.legal.brain.denormalized;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Position;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.legal.brain.common.Evaluator;
import com.leokom.chess.player.legal.brain.common.SideEvaluator;
import com.leokom.chess.player.legal.brain.internal.common.SymmetricEvaluator;

/**
 * Author: Leonid
 * Date-time: 21.10.14 23:03
 */
class ProtectionEvaluator implements Evaluator {
    /**
	 	Protection has 2 aspects:
	 	a) tactical: act when your pieces are under attack
		b) strategical: make the pieces protect each other even against further attacks

		From visual POV it would be more interesting to implement a)
		since it will look like smart behaviour of protection against direct attack.

		Ways to protect a)
		- go away from attack (but be careful not moving to attacked square)
		- capture the attacker (but be careful - it might be protected)
		- protect your piece (so you'll be able to capture the attacker next move
		if the square isn't double-attacked)
		- put another piece in front of yours (if the attacker is not king or knight)
		(probably if it's less valuable piece)

		2 first ways can be described in 'reduction of attacking index of opponent'
		3'd way is more related to strategical protection (but anyway it's a way to act)
		4'th way is a little bit similar to 1-2  (if we take piece value into account)

	 @return non-positive index [ - some big number, 0 ]
	 */
	@Override
	public double evaluateMove( Position position, Move move ) {
		Position targetPosition = position.move(move);
		return new SymmetricEvaluator( new ProtectionSideEvaluator() ).evaluate( targetPosition );
	}

	private class ProtectionSideEvaluator implements SideEvaluator {

		@Override
		public double evaluatePosition(Position position, Side side) {
			//checks level of protection
			return position.getSquaresOccupiedBySide(side).stream().mapToLong(
					square -> position.getSquaresAttackingSquare( side, square ).count()
			).sum();
		}
	}
}