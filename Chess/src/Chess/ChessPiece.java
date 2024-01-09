package Chess;

import BoardGame.Board;

public class ChessPiece extends BoardGame.Piece {
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


}
