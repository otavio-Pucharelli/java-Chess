package Chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import BoardGame.Board;
import BoardGame.Piece;
import BoardGame.Position;
import Chess.pieces.Bishop;
import Chess.pieces.King;
import Chess.pieces.Rook;
import Chess.pieces.Pawn;
import Chess.pieces.Queen;
import Chess.pieces.Knight;


public class ChessMatch {
    private int turn;
    private ColorEnum currentPlayer;
    private Board board;
    private boolean check; //false
    private boolean checkMate; //false
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

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

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }
//-----------------------------------------------//    
    

private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);   
    }

    private void initialSetup() {
        placeNewPiece('a', 1, new Rook(board, ColorEnum.WHITE));
        placeNewPiece('b', 1, new Bishop(board, ColorEnum.WHITE));
        placeNewPiece('c', 1, new Knight(board, ColorEnum.WHITE));
        placeNewPiece('d', 1, new Queen(board, ColorEnum.WHITE));
        placeNewPiece('e', 1, new King(board, ColorEnum.WHITE, this));
        placeNewPiece('f', 1, new Knight(board, ColorEnum.WHITE));
        placeNewPiece('g', 1, new Bishop(board, ColorEnum.WHITE));
        placeNewPiece('h', 1, new Rook(board, ColorEnum.WHITE));
        placeNewPiece('a', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('b', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('c', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('d', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('e', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('f', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('g', 2, new Pawn(board, ColorEnum.WHITE));
        placeNewPiece('h', 2, new Pawn(board, ColorEnum.WHITE));

        placeNewPiece('a', 8, new Rook(board, ColorEnum.BLACK));
        placeNewPiece('b', 8, new Bishop(board, ColorEnum.BLACK));
        placeNewPiece('c', 8, new Knight(board, ColorEnum.BLACK));
        placeNewPiece('d', 8, new Queen(board, ColorEnum.BLACK));
        placeNewPiece('e', 8, new King(board, ColorEnum.BLACK, this));
        placeNewPiece('f', 8, new Knight(board, ColorEnum.BLACK));
        placeNewPiece('g', 8, new Bishop(board, ColorEnum.BLACK));
        placeNewPiece('h', 8, new Rook(board, ColorEnum.BLACK));
        placeNewPiece('a', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('b', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('c', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('d', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('e', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('f', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('g', 7, new Pawn(board, ColorEnum.BLACK));
        placeNewPiece('h', 7, new Pawn(board, ColorEnum.BLACK));
    } 

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
       
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        
        Piece capturedPiece = makeMove(source, target);
        if (testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        ChessPiece movedPiece = (ChessPiece)board.piece(target);

        if (movedPiece instanceof Pawn) {
            if (movedPiece.getColor() == ColorEnum.WHITE && target.getRow() == 0 || movedPiece.getColor() == ColorEnum.BLACK && target.getRow() == 7) {
                promoted = (ChessPiece)board.piece(target);
                promoted = replacePromotedPiece("Q");
            }
            
        }


        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        }
        else {
            nextTurn();
        }

        // specialmove en passant
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        }
        else {
            enPassantVulnerable = null;
        }
        
        return (ChessPiece)capturedPiece;
    } 

    public ChessPiece replacePromotedPiece(String type) {
        if (promoted == null) {
            throw new IllegalStateException("There is no piece to be promoted");
        }
        if (!type.equals("B") && !type.equals("N") && !type.equals("R") & !type.equals("Q")) {
            throw new InvalidParameterException("Invalid type for promotion");
        }

        Position pos = promoted.getChessPosition().toPosition();
        Piece p = board.raemovePiece(pos);
        piecesOnTheBoard.remove(p);

        ChessPiece newPiece = newPiece(type, promoted.getColor());
        board.placePiece(newPiece, pos);
        piecesOnTheBoard.add(newPiece);

        return newPiece;
    }

    private ChessPiece newPiece(String type, ColorEnum color) {
        if (type.equals("B")) return new Bishop(board, color);
        if (type.equals("N")) return new Knight(board, color);
        if (type.equals("Q")) return new Queen(board, color);
        return new Rook(board, color);
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece)board.raemovePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.raemovePiece(target);
        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add((ChessPiece)capturedPiece);    
        }

        // specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.raemovePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.raemovePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }
    
        // specialmove en passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == ColorEnum.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                }
                else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.raemovePiece(pawnPosition);
                capturedPieces.add((ChessPiece)capturedPiece);
                piecesOnTheBoard.remove(capturedPiece);
            }
        }

        board.placePiece(p, target);
        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece)board.raemovePiece(target);
        board.placePiece(p, source);
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);    
        }

        // specialmove castling kingside rook
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece)board.raemovePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // specialmove castling queenside rook
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece)board.raemovePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

                // specialmove en passant
                if (p instanceof Pawn) {
                    if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                        ChessPiece pawn = (ChessPiece)board.raemovePiece(target);
                        Position pawnPosition;
                        if (p.getColor() == ColorEnum.WHITE) {
                            pawnPosition = new Position(3, target.getColumn());
                        }
                        else {
                            pawnPosition = new Position(4, target.getColumn());
                        }
                        board.placePiece(pawn, pawnPosition);

                    }
                }
        
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

    private ColorEnum opponent(ColorEnum color) {
        return (color == ColorEnum.WHITE) ? ColorEnum.BLACK : ColorEnum.WHITE;
    }

    private ChessPiece king(ColorEnum color) {
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private boolean testCheck(ColorEnum color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();
            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(ColorEnum color) {
        if (!testCheck(color)) {
            return false;
        }
        List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for (Piece p : list) {
            boolean[][] mat = p.possibleMoves();
            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (mat[i][j]) {
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);
                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);
                        if (!testCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
}
