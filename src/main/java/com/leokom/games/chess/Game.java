package com.leokom.games.chess;

import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.Side;
import com.leokom.games.chess.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Function;

/**
 * Create &amp; Run Game of Chess.
 * Author: Leonid
 * Date-time: 11.02.16 23:00
 */
//non-final for mocking
public class Game {
	private final Player whitePlayer;
	private final Player blackPlayer;

	private final Logger logger = LogManager.getLogger();

	Game( Function< Side, Player > players ) {
		this(
			players.apply( Side.WHITE ),
			players.apply( Side.BLACK )
		);
	}

	/**
	 * Initiate game between two players
	 * @param whitePlayer white player
	 * @param blackPlayer black player
	 */
	public Game( Player whitePlayer, Player blackPlayer ) {
		this.whitePlayer = whitePlayer;
		this.blackPlayer = blackPlayer;
	}

	/**
	 * Run the game.
	 * @return winner among whitePlayer, blackPlayer or null in case of draw
	 * @deprecated use {@link #runGame()}
	 */
	public Player run() {
		runGame();

		return winner().orElse(null);
	}

	public GameResult runGame() {
		logger.info( "Starting game : {} vs {}", whitePlayer::name, blackPlayer::name );

		//setting opponents for symmetry. Technically it's possible
		// for one set to make a back reference
		blackPlayer.setOpponent( whitePlayer );
		whitePlayer.setOpponent( blackPlayer );

		//Black is informed first, to passively wait for the White's first move
		blackPlayer.opponentSuggestsMeStartNewGameBlack();

		//inform white that black is ready so you may start
		//white player should start the game e.g. by providing main loop
		whitePlayer.opponentSuggestsMeStartNewGameWhite();

		logger.info( "Game finished: {} vs {}", whitePlayer::name, blackPlayer::name );

		return result();
	}

	public GameResult result() {
		//TODO: asymmetry, need validating that blackPlayer position gives same result
		//maybe it's time to share the Position
		//it caused extra complexity in PGNGameTest
		Position position = whitePlayer.getPosition();

		if ( position.isTerminal() ) {
			final Side winningSide = position.getWinningSide();

			if ( winningSide == null ) {
				return GameResult.DRAW;
			} else if ( winningSide == Side.WHITE ) {
				return GameResult.WHITE_WINS;
			}
			else if ( winningSide == Side.BLACK ) {
				return GameResult.BLACK_WINS;
			}
			else {
				throw new IllegalStateException( "Unknown winning side: " + winningSide );
			}

		}
		else {
			return GameResult.UNFINISHED_GAME;
		}
	}

	public Optional<Player> winner() {
		GameResult gameResult = result();
		switch (gameResult) {
			case WHITE_WINS:
				return Optional.of(whitePlayer);
			case BLACK_WINS:
				return Optional.of(blackPlayer);
			case DRAW:
			case UNFINISHED_GAME:
				return Optional.empty();
			default:
				throw new IllegalStateException( "The result is not supported: " + gameResult );
		}
	}

	public Player player(Side side) {
		return side == Side.WHITE ? whitePlayer : blackPlayer;
	}
}
