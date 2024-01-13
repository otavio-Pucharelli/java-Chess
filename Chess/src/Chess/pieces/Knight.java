package Chess.pieces;

import BoardGame.Board;
import BoardGame.Position;
import Chess.ChessPiece;
import Chess.ColorEnum;

public class Knight extends ChessPiece{

    public Knight(Board board, ColorEnum color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "N";
    }

    private boolean canMove(int row, int column) {
        ChessPiece p = (ChessPiece) getBoard().piece(row, column);
        return p == null || p.getColor() != getColor();
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        // above left
        p.setValues(position.getRow() - 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // above right
        p.setValues(position.getRow() - 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // right above
        p.setValues(position.getRow() - 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // right below
        p.setValues(position.getRow() + 1, position.getColumn() + 2);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // below right
        p.setValues(position.getRow() + 2, position.getColumn() + 1);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // below left
        p.setValues(position.getRow() + 2, position.getColumn() - 1);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // left below
        p.setValues(position.getRow() + 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        // left above
        p.setValues(position.getRow() - 1, position.getColumn() - 2);
        if (getBoard().positionExists(p) && canMove(p.getRow(), p.getColumn())) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        return mat;
    }
    
}
