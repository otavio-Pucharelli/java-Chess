package Chess;

import java.util.ArrayList;
import java.util.List;

import BoardGame.Board;
import BoardGame.Piece;
import BoardGame.Position;
import Chess.pieces.Rook;

public class ChessMatch {
    private int turn;
    private ColorEnum currentPlayer;
    private Board board;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = ColorEnum.WHITE;
        initialSetup();
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }


//---------getters and setters------------------//
    public int getTurn() {
        return turn;
    }

    public ColorEnum getCurrentPlayer() {
        return currentPlayer;
    }
//-----------------------------------------------//    
    

private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);   
    }

    private void initialSetup() {
        placeNewPiece('b', 6, new Rook(board, ColorEnum.WHITE));
        placeNewPiece('a', 1, new Rook(board, ColorEnum.BLACK));
    } 

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
       
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        
        Piece capturedPiece = makeMove(source, target);
        nextTurn();
        
        return (ChessPiece)capturedPiece;
    } 

    private Piece makeMove(Position source, Position target) {
        Piece p = board.raemovePiece(source);
        Piece capturedPiece = board.raemovePiece(target);
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add((ChessPiece)capturedPiece);    
        }
        board.placePiece(p, target);
        return capturedPiece;
    }
    
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if(!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if(!board.piece(source).possibleMove(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == ColorEnum.WHITE) ? ColorEnum.BLACK : ColorEnum.WHITE;
    }
}
