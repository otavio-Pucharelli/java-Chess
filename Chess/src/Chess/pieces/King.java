package Chess.pieces;

import BoardGame.Board;
import Chess.ChessPiece;

public class King extends ChessPiece {
        
    public King(Board board, Chess.ColorEnum color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }
    
}
