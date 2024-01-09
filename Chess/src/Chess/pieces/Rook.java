package Chess.pieces;

import BoardGame.Board;
import Chess.ChessPiece;

public class Rook extends ChessPiece{
    
        public Rook(Board board, Chess.ColorEnum color) {
            super(board, color);
        }
    
        @Override
        public String toString() {
            return "R";
        }
    
}