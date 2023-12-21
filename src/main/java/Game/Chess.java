package Game;


import Game.Pieces.*;


public class Chess {
    Piece whiteKing;
    Piece blackKing;

    public Piece lastMovedPiece = null;

    public int moveCount = 1;
    public Color turn = Color.WHITE;

    public Piece[][] board;

    public int[][] allyMoves;
    public int[][] opponentMoves;

    public boolean end = false;

    public String cause = "";

    static final private String[][] template = {
            {"wR", "wN", "wB", "wQ", "wK", "wB", "wN", "wR"},
            {"wP", "wP", "wP", "wP", "wP", "wP", "wP", "wP"},
            {"EE", "EE", "EE", "EE", "EE", "EE", "EE", "EE"},
            {"EE", "EE", "EE", "EE", "EE", "EE", "EE", "EE"},
            {"EE", "EE", "EE", "EE", "EE", "EE", "EE", "EE"},
            {"EE", "EE", "EE", "EE", "EE", "EE", "EE", "EE"},
            {"bP", "bP", "bP", "bP", "bP", "bP", "bP", "bP"},
            {"bR", "bN", "bB", "bQ", "bK", "bB", "bN", "bR"}};

    public Chess() {
        board = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = evaluatePiece(template[i][j], j, i);

            }
        }
        evaluateMoves();
    }


    public void evaluateMoves() {
        Piece king = turn == Color.WHITE ? whiteKing : blackKing;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    if (board[i][j] == king) continue;
                    board[i][j].evaluateMoves();
                }
            }
        }
        opponentMoves = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) continue;
                if (board[i][j].color != turn) addMask(opponentMoves, board[i][j].availableMoves);
            }
        }
        king.evaluateMoves();
        allyMoves = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == null) continue;
                if (board[i][j].color == turn) addMask(allyMoves, board[i][j].availableMoves);
            }
        }

    }


    private Piece evaluatePiece(String pieceCode, int x, int y) {
        Color color = pieceCode.charAt(0) == 'w' ? Color.WHITE : Color.BLACK;
        return switch (pieceCode.charAt(1)) {
            case 'P' -> Piece.createPawn(this, x, y, color);
            case 'R' -> Piece.createRook(this, x, y, color);
            case 'N' -> Piece.createKnight(this, x, y, color);
            case 'B' -> Piece.createBishop(this, x, y, color);
            case 'Q' -> Piece.createQueen(this, x, y, color);
            case 'K' -> {
                Piece king = Piece.createKing(this, x, y, color);
                if (color == Color.WHITE) whiteKing = king;
                else blackKing = king;
                yield king;
            }
            default -> null;
        };
    }

    public Piece getPieceAt(int x, int y) {
        if (x > 7 || x < 0 || y > 7 || y < 0) return null;
        return board[y][x];
    }

    public void addMask(int[][] mask1, int[][] mask2) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mask1[i][j] += mask2[i][j];
            }
        }
    }

    public int sumMask(int[][] mask) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                sum += mask[i][j];
            }
        }
        return sum;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n  A  B  C  D  E  F  G  H   \n");
        for (int i = 7; i >= 0; i--) {
            sb.append(i + 1).append(" ");
            for (int j = 0; j < 8; j++) {
                if (board[i][j] != null) {
                    sb.append(board[i][j].color == Color.WHITE ? 'w' : 'b');
                    switch (board[i][j].type) {
                        case PAWN -> sb.append("P ");
                        case ROOK -> sb.append("R ");
                        case KNIGHT -> sb.append("N ");
                        case BISHOP -> sb.append("B ");
                        case QUEEN -> sb.append("Q ");
                        case KING -> sb.append("K ");
                    }
                } else sb.append("e  ");
            }
            sb.append(i + 1).append(" \n");
        }
        sb.append("  A  B  C  D  E  F  G  H   \n");
        return sb.toString();
    }

    public String move(String move) {
        if (end) {
            return cause;
        }
        if (move.length() < 4) return "INVALID MOVE";
        int x1 = move.charAt(0) - 'a';
        int y1 = move.charAt(1) - '1';
        int x2 = move.charAt(3) - 'a';
        int y2 = move.charAt(4) - '1';

//        for (int i = 7; i >= 0; i--) {
//            for (int j = 0; j < 8; j++) {
//                System.out.print(opponentMoves[i][j] + " ");
//            }
//            System.out.println();
//        }


        Piece piece = getPieceAt(x1, y1);
        if (piece == null) return "THERE IS NO PIECE ON THAT TILE";
        if (piece.color != turn) return "THE TURN IS AT THE OTHER SIDE";
//        piece.printMoves();
        if (piece.availableMoves[y2][x2] == 0) return "WRONG MOVE";

        if (piece.type == Type.PAWN) {
            Piece enPassant = getPieceAt(x1 - 1, y1);
            if (enPassant != null)
                if (enPassant.type == Type.PAWN && x2 == x1 - 1 && enPassant == lastMovedPiece)
                    board[y1][x1 - 1] = null;
            enPassant = getPieceAt(x1 + 1, y1);
            if (enPassant != null)
                if (enPassant.type == Type.PAWN && x2 == x1 + 1 && enPassant == lastMovedPiece)
                    board[y1][x1 + 1] = null;
            if (y1 == 6 && piece.color == Color.WHITE || y1 == 1 && piece.color == Color.BLACK) {
                char type = move.charAt(6);
                String color = piece.color == Color.WHITE ? "w" : "b";
                if (type == 'Q' || type == 'N' || type == 'B' || type == 'R') {
                    System.out.println("PT");
                    Piece newPiece = evaluatePiece(color + type, x1, y1);
                    newPiece.moveCount = piece.moveCount;
                    piece = newPiece;
                } else return "WRONG PAWN TRANSFORMATION";
            }
        } else if (piece.type == Type.KING && Math.abs(x2 - x1) == 2) {
            int dx = 1;
            int r_x = 7;
            if (x2 - x1 < 0) {
                dx = -1;
                r_x = 0;
            }
            Piece rook = board[y1][r_x];
            board[y1][r_x] = null;
            board[y1][x2 - dx] = rook;
            rook.x = x2 - dx;
            rook.moveCount++;
        }


        piece.x = x2;
        piece.y = y2;
        board[y1][x1] = null;
        board[y2][x2] = piece;
        lastMovedPiece = piece;
        piece.moveCount++;
        turn = turn == Color.WHITE ? Color.BLACK : Color.WHITE;
        if (turn == Color.WHITE) moveCount++;
        evaluateMoves();
        if (sumMask(allyMoves) == 0) {
            end = true;
            String win;
            if (turn == Color.WHITE) {
                win = "BLACKS";
                if (opponentMoves[whiteKing.y][whiteKing.x] != 0) cause = "CHECK AND MATE";
                else cause = "STALEMATE";
            } else {
                win = "WHITES";
                if (opponentMoves[blackKing.y][blackKing.x] != 0) cause = "CHECK AND MATE";
                else cause = "STALEMATE";
            }
            cause = win + " WIN BY " + cause;
        }
        return move;
    }

    public String getTurn() {
        return turn == Color.WHITE ? "white" : "black";
    }
}
