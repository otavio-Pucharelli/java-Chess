package Chess;

import BoardGame.Board;
import BoardGame.Position;

public abstract class ChessPiece extends BoardGame.Piece {
    private ColorEnum color;
    private int moveCount;

    public ChessPiece(Board board, ColorEnum color) {
        super(board);
        this.color = color;
    }

    public ColorEnum getColor() {
        return color;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void increaseMoveCount() {
        moveCount++;
    }

    public void decreaseMoveCount() {
        moveCount--;
    }

    protected boolean isThereOpponentPiece(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p != null && p.getColor() != color;
    }

    public ChessPosition getChessPosition() {
        return ChessPosition.fromPosition(position);
    }
}
