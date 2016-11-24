package com.leokom.chess.player.winboard;

import com.leokom.chess.engine.Side;

/**
 * Low-level Winboard-commands for some abstraction and easier testing.
 * The 'on' methods set up listeners to some events received from Winboard server
 * Author: Leonid
 * Date-time: 10.11.12 21:54
 */
interface WinboardCommander {

	void startInit();

	/**
	 * Enable usermove prefixes for moves for easier parsing
	 */
	void enableUserMovePrefixes();

	//signal end of initializations
	void finishInit();

	void agreeToDrawOffer();

	/**
	 * Fetch input from the underlying communication component
	 * Dispatch listeners (call them depending on the input)
	 */
	void processInputFromServer();

	//TODO: add analyze if this command is received immediately after xboard
	//if not - we may assume it's protocol v1

	void onXBoard( XBoardListener listener );
	void opponentMoved( String move );

	void offerDraw();

	void resign();

	/**
	 * Inform Winboard that the side has won by checkmate
	 * @param side winning by checkmate side
	 */
	void checkmate( Side side );

	/**
	 * Inform Winboard that the obligatory draw by moves count
	 * occurred (in chess after July 2014 there is 75 moves rule)
	 * @param movesCount count of moves according to the rule
	 */
	void obligatoryDrawByMovesCount( int movesCount );

	/**
	 * Inform Winboard about stalemate draw
	 */
	void stalemateDraw();

    /**
     * Inform Winboard that an illegal move has been executed
	 * @param winboardMove original move received from Winboard that is illegal
     */
    void illegalMove( String winboardMove );

	void onProtover( ProtoverListener protoverListener );
	void onQuit( QuitListener listener );
	void onGo( GoListener listener );
	void onUserMove( UserMoveListener listener );
	void onOfferDraw( OfferDrawListener listener );
	void onGameOver( GameOverListener listener );
	void onForce( ForceListener listener );
	void onNew( NewListener listener );



}