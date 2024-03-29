package ru.hyst329.helengine

import ru.hyst329.helengine.Global._
import ru.hyst329.helengine.Move.squareToAlgebraic

case class Move(
    from: Square,
    to: Square,
    movingPiece: Piece,
    captures: Piece,
    oldEnPassant: Square,
    oldCastling: CastlingFlags,
    oldHalfmoveCounter: Int,
    promotesTo: Piece
) {
  def castlingToPlain: Option[Move] = this match {
    case Move(_from, _to, WhiteKing, Empty, _, _oldCastling, _, Empty)
        if _from == E1 && _to == G1 && (_oldCastling & WhiteKingSide) != 0 =>
      Some(this.copy(to = F1))
    case Move(_from, _to, WhiteKing, Empty, _, _oldCastling, _, Empty)
        if _from == E1 && _to == C1 && (_oldCastling & WhiteQueenSide) != 0 =>
      Some(this.copy(to = D1))
    case Move(_from, _to, BlackKing, Empty, _, _oldCastling, _, Empty)
        if _from == E8 && _to == G8 && (_oldCastling & BlackKingSide) != 0 =>
      Some(this.copy(to = F8))
    case Move(_from, _to, BlackKing, Empty, _, _oldCastling, _, Empty)
        if _from == E8 && _to == C8 && (_oldCastling & BlackQueenSide) != 0 =>
      Some(this.copy(to = D8))
    case _ => None
  }

  override def toString: String = s"${squareToAlgebraic(from)}${squareToAlgebraic(to)}"
}

object Move {
  val PieceAbbreviations: Map[Piece, String] = Map(
    Empty       -> "",
    WhitePawn   -> "",
    WhiteKnight -> "N",
    WhiteBishop -> "B",
    WhiteRook   -> "R",
    WhiteQueen  -> "Q",
    WhiteKing   -> "K",
    BlackPawn   -> "",
    BlackKnight -> "N",
    BlackBishop -> "B",
    BlackRook   -> "R",
    BlackQueen  -> "Q",
    BlackKing   -> "K"
  )

  def squareToAlgebraic(square: Square) = s"${('a' + square % 8).toChar}${square / 8 + 1}"

  def fromBoardContext(board: Board, from: Square, to: Square, promotesTo: Piece = Empty) = Move(
    from,
    to,
    board.getPiece(from),
    board.getPiece(to),
    board.enPassantSquare,
    board.castlingFlags,
    board.halfmoveCounter,
    promotesTo
  )

  def toNotation(move: Move, board: Board): String = {
    val fromPiece = move.movingPiece
    val toPiece   = move.captures
    toPiece match {
      case Empty =>
        s"${PieceAbbreviations(fromPiece)}${squareToAlgebraic(move.from)}-${squareToAlgebraic(move.to)}"
      case _ =>
        s"${PieceAbbreviations(fromPiece)}${squareToAlgebraic(move.from)}x${squareToAlgebraic(move.to)}"
    }
  }
}
