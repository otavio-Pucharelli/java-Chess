package program;

import Chess.ChessMatch;

public class App {
    
    public static void main(String[] args) throws Exception {
        
        ChessMatch chessMatch = new ChessMatch();
        UI.PrintBoard(chessMatch.getPieces());

    }
}
