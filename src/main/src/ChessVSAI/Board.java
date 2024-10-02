package ChessVSAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Board extends JFrame {
    private final Tile[][] tiles = new Tile[8][8];
    private final ArrayList<Move> moveList = new ArrayList<>();
    private final int[][] gameState = new int[8][8];
    Board() {
        super("Chess_1VS1");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(8, 8));
        this.setResizable(false);
        boolean swap = true;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tiles[i][j] = new Tile();
                this.add(tiles[i][j]);
                tiles[i][j].addMouseListener(new TileMouseListener(tiles[i][j]));
                if (swap) {
                    tiles[i][j].setBackground(new Color(245, 222, 179));
                    swap = false;
                }
                else {
                    tiles[i][j].setBackground(new Color(139, 69, 19));
                    swap = true;
                }
            }
            swap = !swap;
        }
        setUpGame();
        this.setVisible(true);
    }

    public void setUpGame() {
        setUpBlackSide();
        setUpWhiteSide();
        updateGameState();
    }

    private void setUpBlackSide() {
        boolean pawns = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (!pawns) {
                    switch (j) {
                        case 0:
                        case 7:
                            tiles[i][j].updateTile(new Rook(Team.BLACK));
                            break;
                        case 1:
                        case 6:
                            tiles[i][j].updateTile(new Knight(Team.BLACK));
                            break;
                        case 2:
                        case 5:
                            tiles[i][j].updateTile(new Bishop(Team.BLACK));
                            break;
                        case 3:
                            tiles[i][j].updateTile(new Queen(Team.BLACK));
                            break;
                        case 4:
                            tiles[i][j].updateTile(new King(Team.BLACK));
                    }
                } else {
                    tiles[i][j].updateTile(new Pawn(Team.BLACK));
                }
            }
            pawns = true;
        }
    }

    private void setUpWhiteSide() {
        boolean pawns = true;
        for (int i = 6; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!pawns) {
                    switch (j) {
                        case 0:
                        case 7:
                            tiles[i][j].updateTile(new Rook(Team.WHITE));
                            break;
                        case 1:
                        case 6:
                            tiles[i][j].updateTile(new Knight(Team.WHITE));
                            break;
                        case 2:
                        case 5:
                            tiles[i][j].updateTile(new Bishop(Team.WHITE));
                            break;
                        case 3:
                            tiles[i][j].updateTile(new Queen(Team.WHITE));
                            break;
                        case 4:
                            tiles[i][j].updateTile(new King(Team.WHITE));
                    }
                } else {
                    tiles[i][j].updateTile(new Pawn(Team.WHITE));
                }
            }
            pawns = false;
        }
    }

    public void updateGameState() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (!tiles[i][j].getPiece().isDead())
                    gameState[i][j] = 1;
    }

    public void printGameState() {
        for (int[] row : gameState) {
            for (int i : row) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }

    private class TileMouseListener extends MouseAdapter {
        private final Tile tile;

        TileMouseListener(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Tile sourceTile = (Tile) e.getSource();
            if (!sourceTile.getPiece().isDead() && sourceTile.getPiece().getSide() == Team.WHITE && !sourceTile.isClicked()) {
                if (checkActivations()) {
                    disableAllActivations();
                    disableAllMoveIndicators();
                }
                sourceTile.setMoveSource();
                switch (sourceTile.getPiece().getType()) {
                    case PAWN, KNIGHT, KING:
                        showAvailableMoves();
                        break;
                    case BISHOP, ROOK, QUEEN:
                        showAvailableMovesIntervall();
                        break;
                }
            }
            else if(sourceTile.isClicked()) {
                sourceTile.setMoveSource();
                disableAllMoveIndicators();
            }
            else if (isValidMoveTile(sourceTile)) {
                Move move = new Move(
                        getSourceIndex(tile.getOwner()),
                        tile.getOwner().getPiece(),
                        getSourceIndex(tile),
                        tile.getPiece(),
                        tiles
                );
                executeMove(move);
            }
            else {
                disableAllActivations();
                disableAllMoveIndicators();
            }
        }

        private boolean isValidMoveTile(Tile tile) {
            Piece piece = tile.getPiece();
            boolean b1 = piece.isDead() && tile.isMoveablePosition();
            boolean b2 = !piece.isDead() && tile.isMoveablePosition() && piece.getSide() == Team.BLACK;
            return (b1 || b2);
        }

        public boolean checkActivations() {
            for (Tile[] row : tiles)
                for (Tile i : row)
                    if (i.isClicked() && i.getPiece().getType() != ChessVSAI.Type.PIECE)
                        return true;
            return false;
        }

        public void disableAllActivations() {
            for (Tile[] row : tiles) {
                for (Tile i : row) {
                    if (i.isClicked()) {
                        i.setClicked();
                        i.removeCircle();

                    }
                }
            }
        }

        public void disableAllMoveIndicators() {
            for (Tile[] row : tiles)
                for (Tile i : row)
                    if (i.getPiece().isDead() || i.getPiece().getSide() == Team.BLACK)
                        i.removeCircle();
        }

        public <T extends Piece>void showAvailableMoves() {
            @SuppressWarnings("unchecked")
            T t = (T) tile.getPiece();
            int[] sourceIndex = getSourceIndex(tile);
            int[][] legalMoves = t.getLegalMoves();
            boolean friendly = false;
            for (int[] legalMove : legalMoves) {
                int targetY = sourceIndex[0] + legalMove[0];
                int targetX = sourceIndex[1] + legalMove[1];
                int[] targetArr = {targetY, targetX};
                if (t.getType() == ChessVSAI.Type.PAWN) {
                    if (legalMove[0] == -1 && legalMove[1] == 0 && checkIfAllyPiece(targetY,targetX) && ((Pawn)t).isStart())
                        friendly = true;
                    if (!validateMovesPawn(legalMove, targetY, targetX, friendly)) {
                        continue;
                    }
                }
                else if (t.getType() == ChessVSAI.Type.KNIGHT || t.getType() == ChessVSAI.Type.KING) {
                    if (checkIfAllyPiece(targetY,targetX))
                        continue;
                }
                if (checkWithinBounds(targetArr))
                    processMove(targetArr, tile);
            }
        }

        public <T extends Piece>void showAvailableMovesIntervall() {
            @SuppressWarnings("unchecked")
            T t = (T) tile.getPiece();
            int[] sourceIndex = getSourceIndex(tile);
            int[][] legalMoves = t.getLegalMoves();
            int targetY = sourceIndex[0];
            int targetX = sourceIndex[1];
            boolean seenEnemyPiece = false;
            boolean seenFriendlyPiece = false;
            for (int[] legalMove : legalMoves) {
                while (checkWithinBounds(targetY, targetX)) {
                    targetY += legalMove[0];
                    targetX += legalMove[1];
                    int[] targetArr = {targetY, targetX};
                    if (checkIfAllyPiece(targetY, targetX)) {
                        seenFriendlyPiece = true;
                        continue;
                    }
                    if (checkWithinBounds(targetArr)) {
                        if (seenEnemyPiece || seenFriendlyPiece)
                            break;
                        if (tiles[targetY][targetX].getPiece().getSide() == Team.BLACK)
                            seenEnemyPiece = true;
                        processMove(targetArr, tile);
                    }
                }
                targetY = sourceIndex[0];
                targetX = sourceIndex[1];
                seenEnemyPiece = false;
                seenFriendlyPiece = false;
            }
        }

        public boolean validateMovesPawn(int[] legalMove, int targetY, int targetX, boolean friendly) {
            if ((legalMove[0] == -2 && !((Pawn) tile.getPiece()).isStart()) || checkIfAllyPiece(targetY, targetX))
                return false;
            if ((legalMove[1] == -1 && !checkIfPieceOn(targetY, targetX)))
                return false;
            if ((legalMove[1] == 1 && !checkIfPieceOn(targetY, targetX)))
                return false;
            if (legalMove[0] == -2 && legalMove[1] == 0 && friendly && ((Pawn) tile.getPiece()).isStart())
                return false;
            return legalMove[0] != -1 || legalMove[1] != 0 || !checkIfPieceOn(targetY, targetX);
        }

        public boolean checkIfPieceOn(int targetY, int targetX) {
            if (checkWithinBounds(targetY,targetX)) {
                return tiles[targetY][targetX].isPieceOn();
            }
            return false;
        }

        public boolean checkIfAllyPiece(int targetY, int targetX) {
            if (checkWithinBounds(targetY,targetX)) {
                return tiles[targetY][targetX].isPieceOn() && tiles[targetY][targetX].getPiece().getSide() == Team.WHITE;
            }
            return false;
        }

        public void processMove(int[] dest, Tile tile) {
            Tile destTile = tiles[dest[0]][dest[1]];
            destTile.setOwner(tile);
            destTile.setCanMoveto();
        }

        public boolean checkWithinBounds(int[] source) {
            return source[0] < 8 && source[0] >= 0 && source[1] < 8 && source[1] >= 0;
        }

        public boolean checkWithinBounds(int y, int x) {
            return y < 8 && y >= 0 && x < 8 && x >= 0;
        }

        public int[] getSourceIndex(Tile tile) {
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++)
                    if (tiles[i][j].equals(tile))
                        return new int[]{i, j};
            return new int[]{};
        }

        public void executeMove(Move move) {
            if(move.getSourcePiece().getType() == ChessVSAI.Type.PAWN) {
                if (move.getDestY() == move.getSourceY() - 2) {
                    ((Pawn) move.getSourcePiece()).turnOffStart();
                }
                else if (move.getDestY() == move.getSourceY() - 1) {
                    ((Pawn) move.getSourcePiece()).turnOffStart();
                }
            }
            tiles[move.getDestY()][move.getDestX()].updateTile(move.getSourcePiece());
            tiles[move.getSourceY()][move.getSourceX()].removePiece();
            disableAllMoveIndicators();
        }
    }
}
