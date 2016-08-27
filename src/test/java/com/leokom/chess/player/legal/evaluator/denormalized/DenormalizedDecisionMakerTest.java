package com.leokom.chess.player.legal.evaluator.denormalized;

import com.leokom.chess.engine.Position;
import com.leokom.chess.player.legal.evaluator.common.DecisionMaker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Author: Leonid
 * Date-time: 27.08.16 21:58
 */
public class DenormalizedDecisionMakerTest {
	private DecisionMaker decisionMaker;

	@Before
	public void prepare() {
		decisionMaker = new DenormalizedDecisionMaker();
	}

	@Test
	public void canFindMoves() {
		assertTrue( decisionMaker.findBestMove( Position.getInitialPosition() ).isPresent() );
	}

}