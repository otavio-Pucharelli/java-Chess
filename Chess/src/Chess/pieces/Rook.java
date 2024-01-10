package Chess.pieces;

import BoardGame.Board;
import BoardGame.Position;
import Chess.ChessPiece;

public class Rook extends ChessPiece{
    
        public Rook(Board board, Chess.ColorEnum color) {
            super(board, color);
        }
    
        @Override
        public String toString() {
            return "R";
        }

        @Override
        public boolean[][] possibleMoves() {
            boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

            Position p = new Position(0, 0); // auxiliar position

            p.setValues(position.getRow() - 1, position.getColumn()); // above
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                p.setRow(p.getRow() - 1);
                
            }
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                
            }

            p.setValues(position.getRow(), position.getColumn() - 1); // left
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                p.setColumn(p.getColumn() - 1);
                
            }
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                
            }


            p.setValues(position.getRow(), position.getColumn() + 1); // right
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                p.setColumn(p.getColumn() + 1);
                
            }
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                
            }

            p.setValues(position.getRow() + 1, position.getColumn()); // below
            while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                p.setRow(p.getRow() + 1);
                
            }
            if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
                mat[p.getRow()][p.getColumn()] = true;
                
            }
            
            return mat;
        }
    
}
