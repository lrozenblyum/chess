package com.leokom.chess.engine.validator;

import com.leokom.chess.engine.Move;
import com.leokom.chess.engine.Side;
import com.leokom.chess.player.Player;

/**
 * Author: Leonid
 * Date-time: 16.11.14 18:20
 *
 * Validate player's actions in different situations.
 *
 * Interesting architectural solution which I decided to do
 * is implementing the same Player interface
 * and inject it as man-in-the-middle
 *
 * It's the first time engine depends on Player (good?)
 *
 */
public class Validator implements Player {
	private final Player whitePlayer;
	private final Player blackPlayer;

	private Side sideToMove = Side.WHITE;

	public Validator( Player white, Player black ) {

		this.whitePlayer = white;
		this.blackPlayer = black;
	}

	@Override
	public void opponentOfferedDraw() {

	}

	@Override
	public void opponentAgreedToDrawOffer() {

	}

	@Override
	public void opponentSuggestsMeStartNewGameWhite() {

	}

	@Override
	public void opponentMoved( Move opponentMove ) {
		sideToMove = sideToMove.opposite();

		//TODO: a little bit better implementation
		//but it has issues : when a player goes when he is not allowed
		//(not his turn) we'll pass it
		if ( sideToMove == Side.BLACK ) {
			blackPlayer.opponentMoved( opponentMove );
		}
		else {
			whitePlayer.opponentMoved( opponentMove );
		}
	}

	@Override
	public void opponentResigned() {

	}

	@Override
	public void setOpponent( Player opponent ) {

	}
}
