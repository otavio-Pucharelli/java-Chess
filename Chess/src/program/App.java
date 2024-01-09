import BoardGame.Position;
import Chess.ChessMatch;
import BoardGame.Board;
import BoardGame.Piece;

public class App {
    
    public static void main(String[] args) throws Exception {
        
        ChessMatch chessMatch = new ChessMatch();
        UI.PrintBoard(chessMatch.getPieces());

    }
}
