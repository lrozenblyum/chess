package com.leokom.games.chess.player.uci;

import com.fluxchess.jcpi.AbstractEngine;
import com.fluxchess.jcpi.commands.*;
import com.fluxchess.jcpi.models.*;
import com.leokom.games.chess.engine.Move;
import com.leokom.games.chess.engine.PieceType;
import com.leokom.games.chess.engine.Position;
import com.leokom.games.chess.engine.Side;

import java.util.Set;

public final class UciPlayer extends AbstractEngine {

  private Position position = Position.getInitialPosition();
  public static void main(String[] args) {
    new UciPlayer().run();
  }

  @Override
  protected void quit() {
    new EngineStopCalculatingCommand().accept(this);
  }

  @Override
  public void receive(EngineInitializeRequestCommand command) {
    new EngineStopCalculatingCommand().accept(this);

    ProtocolInitializeAnswerCommand answerCommand = new ProtocolInitializeAnswerCommand(
        "UciPlayer 1.1", "Leonid Rozenblyum"
    );

    getProtocol().send(answerCommand);
  }

  @Override
  public void receive(EngineSetOptionCommand command) {
    // TODO
  }

  @Override
  public void receive(EngineDebugCommand command) {
    // TODO
  }

  @Override
  public void receive(EngineReadyRequestCommand command) {
    getProtocol().send(new ProtocolReadyAnswerCommand(command.token));
  }

  @Override
  public void receive(EngineNewGameCommand command) {
    new EngineStopCalculatingCommand().accept(this);

    position = Position.getInitialPosition();
  }

  @Override
  public void receive(EngineAnalyzeCommand command) {
    new EngineStopCalculatingCommand().accept(this);

	  position = new Position( command.board.getActiveColor() == GenericColor.WHITE ? Side.WHITE : Side.BLACK );

    for (GenericPosition genericPosition : GenericPosition.values()) {
      GenericPiece genericPiece = command.board.getPiece(genericPosition);
      if (genericPiece != null) {
        Side side = genericPiece.color == GenericColor.WHITE ? Side.WHITE : Side.BLACK;
        String square = genericPosition.toString();
        PieceType pieceType;
        switch (genericPiece.chessman) {
          case PAWN:
            pieceType = PieceType.PAWN;
            break;
          case KNIGHT:
            pieceType = PieceType.KNIGHT;
            break;
          case BISHOP:
            pieceType = PieceType.BISHOP;
            break;
          case ROOK:
            pieceType = PieceType.ROOK;
            break;
          case QUEEN:
            pieceType = PieceType.QUEEN;
            break;
          case KING:
            pieceType = PieceType.KING;
            break;
          default:
            throw new IllegalArgumentException();
        }
        position.add(side, square, pieceType);
      }
    }


    for (GenericMove genericMove : command.moves) {
      position = position.move(
          genericMove.from.toString(),
          genericMove.to.toString() + (genericMove.promotion == null ? "" : genericMove.promotion.toCharAlgebraic()));
    }
  }

  @Override
  public void receive(EngineStartCalculatingCommand command) {
    new EngineStopCalculatingCommand().accept(this);

    Set<Move> moves = position.getMoves();
    if (!moves.isEmpty()) {
      Move possibleMove = moves.iterator().next();

      final String from = possibleMove.getFrom();
      final String to = possibleMove.getTo();

      GenericPosition fromSquare = GenericPosition.valueOf(from);
      GenericPosition toSquare;
      GenericChessman promotion;
      if (to.length() == 3) {
        toSquare = GenericPosition.valueOf(to.substring(0, 2));
        promotion = GenericChessman.valueOfPromotion(to.charAt(2));
      } else {
        toSquare = GenericPosition.valueOf(to);
        promotion = null;
      }

      GenericMove genericMove = new GenericMove(fromSquare, toSquare, promotion);
      getProtocol().send(new ProtocolBestMoveCommand(genericMove, null));
    }
  }

  @Override
  public void receive(EngineStopCalculatingCommand command) {
    // TODO
  }

  @Override
  public void receive(EnginePonderHitCommand command) {
    // TODO
  }

}
