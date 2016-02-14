package com.leokom.chess.player.legalMover;

import com.leokom.chess.engine.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


public class ProtectionEvaluatorTest {
	private EvaluatorAsserts asserts;

	@Before
	public void prepare() {
		ProtectionEvaluator evaluator = new ProtectionEvaluator();
		asserts = new EvaluatorAsserts( evaluator );
	}

	@Test
	public void leaveAttackedSquare() {
		PositionBuilder position = new PositionBuilder();
		position.add( Side.WHITE, "h8", PieceType.ROOK );
		position.add( Side.WHITE, "c2", PieceType.PAWN );

		position.add( Side.BLACK, "g6", PieceType.KNIGHT ); //attacks the rook

		Move leavingAttackedSquare = new Move( "h8", "b8" );

		Move stayingCalm = new Move( "c2", "c3" );

		asserts.assertFirstBetter( position, leavingAttackedSquare, stayingCalm );
	}

	@Test
	public void doubleAttackMeansNeedToAct() {
		Position position = new Position( Side.BLACK );
		//attacks b3, d3
		position.add( Side.WHITE, "c1", PieceType.KNIGHT );

		position.add( Side.BLACK, "b3", PieceType.PAWN );
		position.add( Side.BLACK, "d3", PieceType.PAWN );

		position.add( Side.BLACK, "g1", PieceType.KING );

		Move leaveOneOfAttacked = new Move( "b3", "b2" );
		Move ignoreAttack = new Move( "g1", "h1" );

		asserts.assertFirstBetter( position, leaveOneOfAttacked, ignoreAttack );
	}

	@Test
	public void protectionByIntersection() {
		Position position = new Position( Side.BLACK );
		position.add( Side.WHITE, "a1", PieceType.QUEEN );

		position.add( Side.BLACK, "h5", PieceType.KNIGHT );
		position.add( Side.BLACK, "h8", PieceType.QUEEN );

		Move knightProtectsQueen = new Move( "h5", "g7" );
		Move noQueenProtection = new Move( "h5", "g3" );

		asserts.assertFirstBetter( position, knightProtectsQueen, noQueenProtection );
	}

	@Test
	public void protectionByDefending() {
		PositionBuilder position = new PositionBuilder()
				.add( Side.WHITE, "a1", PieceType.QUEEN )
				.add( Side.BLACK, "h4", PieceType.KNIGHT )
				.add( Side.BLACK, "h8", PieceType.QUEEN );

		Move knightDefendsQueen = new Move( "h4", "g6" );
		Move noDefenseForQueen = new Move( "h4", "g2" );

		asserts.assertFirstBetter( position, knightDefendsQueen, noDefenseForQueen );
	}

	@Test
	public void protectingMoreValuablePieceImportant() {
		PositionBuilder position = new PositionBuilder()
			.add( Side.WHITE, "a1", PieceType.QUEEN )
			.add( Side.WHITE, "b4", PieceType.BISHOP )
			.add( Side.WHITE, "h8", PieceType.ROOK )
			.add( Side.BLACK, "c2", PieceType.KNIGHT );

		//or to h1
		Move rookDefendsQueen = new Move( "h8", "a8" );
		//or to h4
		Move rookDefendsBishop = new Move( "h8", "b8" );

		asserts.assertFirstBetter( position, rookDefendsQueen, rookDefendsBishop );
	}

	@Ignore( "The requirement is questionable" )
	@Test
	public void protectingByLessValuablePieceIsBetter() {
		PositionBuilder position = new PositionBuilder()
				.add( Side.WHITE, "d4", PieceType.PAWN )
				.add( Side.WHITE, "e6", PieceType.PAWN )
				.add( Side.WHITE, "h1", PieceType.ROOK )
				//attacks e6
				.add( Side.BLACK, "f7", PieceType.PAWN );

		Move pawnDefendsPawn = new Move( "d4", "d5" );
		Move rookDefendsPawn = new Move( "h1", "e1" );

		asserts.assertFirstBetter( position, pawnDefendsPawn, rookDefendsPawn );
	}

	@Test
	public void protectionByMultipleIsBetter() {
		PositionBuilder position = new PositionBuilder()
				.add( Side.WHITE, "c4", PieceType.PAWN )
				//defends c4
				.add( Side.WHITE, "d3", PieceType.PAWN )
				.add( Side.WHITE, "h1", PieceType.ROOK )
				//attacks c4
				.add( Side.BLACK, "d5", PieceType.PAWN );

		Move rookAlsoDefendsC4 = new Move( "h1", "c1" );
		Move justPawnDefendsC4 = new Move( "h1", "g1" );

		asserts.assertFirstBetter( position, rookAlsoDefendsC4, justPawnDefendsC4 );
	}

	//backlog


	// 1) protecting a piece by another piece better than not
	// a) + reactive
	// b) proactive - when no attack
	// Maybe fact of attack should increase value of protective moves?)

	// 2) + Protecting a more valuable piece is more important than less valuable
	// 3) -/* (suspended) Protecting BY less valuable piece is better than BY more valuable

	// 4) * Double protection is better than single
	// 5) - if a piece is attacked by 2, we need more protection

	// 6) Protecting is backwards (against capture)
	// and blocking (prevent attack by crossing)
	//difference?
}