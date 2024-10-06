package Chess_1VS1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class BoardClient extends JFrame {
    private final Tile[][] tiles = new Tile[8][8];
    private final ArrayList<Move> moveList = new ArrayList<>();
    private final int[][] gameState = new int[8][8];
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final Team team;
    private final Team enemyTeam;
    private boolean turn;
    BoardClient(Team team) {
        super("Chess_1VS1");
        this.setSize(600, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(8, 8));
        this.setResizable(false);
        this.team = team;
        if (team == Team.WHITE)
            enemyTeam = Team.BLACK;
        else
            enemyTeam = Team.WHITE;
        try {
            socket = new Socket(InetAddress.getLocalHost(), 6000);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e) {
            System.err.println("IO-Exception: " + e.getMessage());
        }
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
        setUpBottomSide();
        setUpTopSide();
        updateGameState();
        receiveCommands();
        setKingZonesTeam();
        setKingZonesEnemy();
    }

    private void setUpTopSide() {
        boolean pawns = false;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 8; j++) {
                if (!pawns) {
                    switch (j) {
                        case 0:
                        case 7:
                            tiles[i][j].updateTile(new Rook(enemyTeam));
                            break;
                        case 1:
                        case 6:
                            tiles[i][j].updateTile(new Knight(enemyTeam));
                            break;
                        case 2:
                        case 5:
                            tiles[i][j].updateTile(new Bishop(enemyTeam));
                            break;
                        case 3:
                            tiles[i][j].updateTile(new Queen(enemyTeam));
                            break;
                        case 4:
                            tiles[i][j].updateTile(new King(enemyTeam));
                    }
                }
                else {
                    tiles[i][j].updateTile(new Pawn(enemyTeam));
                }
            }
            pawns = true;
        }
    }

    public void receiveCommands() {
        Thread thread = new Thread(() -> {
            try {
                String ans;
                String source;
                String dest;
                while ((ans = in.readLine()) != null) {
                    if (ans.contains("check")) {
                        Tile kingTile = findKingTile(team);
                        kingTile.setCheck(true);
                    }
                    if (ans.contains("ZONES")) {
                        setKingZonesTeam();
                        setKingZonesEnemy();
                    }
                    if (ans.contains("KING_NOT_ALLOWED")) {
                        String cord = ans.substring(16);
                        int[] des = Parser.fromCordToIndex(Parser.invertCords(cord));
                        tiles[des[0]][des[1]].setAllowed(false);
                    }
                    if (ans.contains("mark"))
                        markKingThreats();
                    if (ans.contains("turn on"))
                        turn = true;
                    if (ans.contains("to")) {
                        source = ans.substring(0, 2);
                        dest = ans.substring(6, 8);
                        executeMove(source,dest);
                    }
                }
            }
            catch (IOException e) {
                System.err.println("IO-Exception: " + e.getMessage());
            }
            finally {
                try {
                    socket.close();
                    in.close();
                    out.close();
                }
                catch (IOException e) {
                    System.err.println("IO-Exception: " + e.getMessage());
                }
            }
        });
        thread.start();
    }

    public void sendCommand(String source, String destination) {
        out.println(Parser.invertCords(source) + " to " + Parser.invertCords(destination));
        out.flush();
    }

    public void sendCommand(String command) {
        out.println(command);
        out.flush();
    }

    private void setUpBottomSide() {
        boolean pawns = true;
        for (int i = 6; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!pawns) {
                    switch (j) {
                        case 0:
                        case 7:
                            tiles[i][j].updateTile(new Rook(team));
                            break;
                        case 1:
                        case 6:
                            tiles[i][j].updateTile(new Knight(team));
                            break;
                        case 2:
                        case 5:
                            tiles[i][j].updateTile(new Bishop(team));
                            break;
                        case 3:
                            tiles[i][j].updateTile(new Queen(team));
                            break;
                        case 4:
                            tiles[i][j].updateTile(new King(team));
                    }
                } else {
                    tiles[i][j].updateTile(new Pawn(team));
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

    public boolean checkActivations() {
        for (Tile[] row : tiles)
            for (Tile i : row)
                if (i.isClicked() && i.getPiece().getType() != Chess_1VS1.Type.PIECE)
                    return true;
        return false;
    }

    public void disableAllMoveIndicators() {
        for (Tile[] row : tiles)
            for (Tile i : row)
                if (i.getPiece().isDead() || i.getPiece().getSide() == enemyTeam)
                    i.removeCircle();
    }

    public void setNotMovable() {
        for (Tile[] row : tiles)
            for (Tile i : row)
                i.setMoveablePositionFalse();
    }

    public void markKingThreats() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.isPieceOn() && t.getPiece().getSide() == team) {
                    switch (t.getPiece().getType()) {
                        case PAWN, KNIGHT, KING:
                            showAvailableMoves(t, true);
                            break;
                        case BISHOP, ROOK, QUEEN:
                            showAvailableMovesIntervall(t, true);
                            break;
                    }
                    markKing();
                }
            }
        }
        setNotMovable();
    }

    private void markKing() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.isKingzone() && t.hasOwner() && t.getOwner().getPiece().getSide() == enemyTeam && t.isMoveablePosition()) {
                    sendCommand("KING_NOT_ALLOWED" + Parser.fromIndextoCord(getSourceIndex(t)));
                }
            }
        }
        setNotMovable();
    }

    public void check() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.isPieceOn() && t.getPiece().getSide() == enemyTeam && t.getPiece().getType() == Chess_1VS1.Type.KING && t.isMoveablePosition())
                    sendCommand("check");
            }
        }
    }


    public void disableKingZones() {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.isKingzone()) {
                    t.setKingzone(false);
                    t.setOwner(null);
                }
            }
        }
    }

    public <T extends Piece>void showAvailableMoves(Tile tile, boolean search) {
        @SuppressWarnings("unchecked")
        T t = (T) tile.getPiece();
        int[] sourceIndex = getSourceIndex(tile);
        int[][] legalMoves = t.getLegalMoves();
        boolean friendly = false;
        for (int[] legalMove : legalMoves) {
            int targetY = sourceIndex[0] + legalMove[0];
            int targetX = sourceIndex[1] + legalMove[1];
            int[] targetArr = {targetY, targetX};
            if (t.getType() == Chess_1VS1.Type.PAWN) {
                if (legalMove[0] == -1 && legalMove[1] == 0 && checkIfAllyPiece(targetY,targetX) && ((Pawn)t).isStart())
                    friendly = true;
                if (!validateMovesPawn(legalMove, targetY, targetX, friendly, tile, search)) {
                    continue;
                }
            }
            else if (t.getType() == Chess_1VS1.Type.KNIGHT) {
                if (checkIfAllyPiece(targetY,targetX))
                    continue;
            }
            else if(t.getType() == Chess_1VS1.Type.KING) {
                if (checkIfAllyPiece(targetY,targetX))
                    continue;
                if (BoardClient.checkWithinBounds(targetY,targetX))
                    if (!tiles[targetY][targetX].isAllowed())
                        continue;
            }

            if (BoardClient.checkWithinBounds(targetArr)) {
                if(!search)
                    processMove(targetArr, tile);
                else
                    searchPos(targetArr);
            }
        }
    }

    public <T extends Piece>void showAvailableMovesIntervall(Tile tile, boolean search) {
        @SuppressWarnings("unchecked")
        T t = (T) tile.getPiece();
        int[] sourceIndex = getSourceIndex(tile);
        int[][] legalMoves = t.getLegalMoves();
        int targetY = sourceIndex[0];
        int targetX = sourceIndex[1];
        boolean seenEnemyPiece = false;
        boolean seenFriendlyPiece = false;
        for (int[] legalMove : legalMoves) {
            while (BoardClient.checkWithinBounds(targetY, targetX)) {
                targetY += legalMove[0];
                targetX += legalMove[1];
                int[] targetArr = {targetY, targetX};
                if (checkIfAllyPiece(targetY, targetX)) {
                    seenFriendlyPiece = true;
                    continue;
                }
                if (BoardClient.checkWithinBounds(targetArr)) {
                    if (seenEnemyPiece || seenFriendlyPiece)
                        break;
                    if (tiles[targetY][targetX].getPiece().getSide() == enemyTeam)
                        seenEnemyPiece = true;
                    if(!search)
                        processMove(targetArr, tile);
                    else
                        searchPos(targetArr);
                }
            }
            targetY = sourceIndex[0];
            targetX = sourceIndex[1];
            seenEnemyPiece = false;
            seenFriendlyPiece = false;
        }
    }

    public static boolean checkWithinBounds(int[] source) {
        return source[0] < 8 && source[0] >= 0 && source[1] < 8 && source[1] >= 0;
    }

    public static boolean checkWithinBounds(int y, int x) {
        return y < 8 && y >= 0 && x < 8 && x >= 0;
    }

    public int[] getSourceIndex(Tile tile) {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (tiles[i][j].equals(tile))
                    return new int[]{i, j};
        return new int[]{};
    }

    public String getSourceCords(Tile tile) {
        int[] arr = getSourceIndex(tile);
        return Parser.fromIndextoCord(arr);
    }

    private boolean isValidMoveTile(Tile tile) {
        Piece piece = tile.getPiece();
        boolean b1 = piece.isDead() && tile.isMoveablePosition();
        boolean b2 = !piece.isDead() && tile.isMoveablePosition() && piece.getSide() == enemyTeam;
        return (b1 || b2);
    }


    public boolean validateMovesPawn(int[] legalMove, int targetY, int targetX, boolean friendly, Tile tile, boolean search) {
        Pawn pawn = (Pawn) tile.getPiece();
        // check first diagonal
        if (legalMove[0] == -1 && legalMove[1] == -1 && !search) {
            if (!checkIfPieceOn(targetY,targetX) || checkIfAllyPiece(targetY,targetX))
                return false;
            return true;
        }
        // check second diagonal
        if (legalMove[0] == -1 && legalMove[1] == 1 && !search) {
            if (!checkIfPieceOn(targetY,targetX) || checkIfAllyPiece(targetY,targetX))
                return false;
            return true;
        }

        // check first move
        if (legalMove[0] == -2) {
            if (!pawn.isStart() || checkIfPieceOn(targetY,targetX) || friendly)
                return false;
            return true;
        }

        if (legalMove[0] == -1 && legalMove[1] == 0) {
            if (checkIfPieceOn(targetY,targetX) || friendly)
                return false;
            return true;
        }
        return true;
    }

    public boolean checkIfPieceOn(int targetY, int targetX) {
        if (BoardClient.checkWithinBounds(targetY,targetX))
            return tiles[targetY][targetX].isPieceOn();
        return false;
    }

    public boolean checkIfAllyPiece(int targetY, int targetX) {
        if (BoardClient.checkWithinBounds(targetY,targetX)) {
            return tiles[targetY][targetX].isPieceOn() && tiles[targetY][targetX].getPiece().getSide() == team;
        }
        return false;
    }



    public void processMove(int[] dest, Tile tile) {
        Tile destTile = tiles[dest[0]][dest[1]];
        destTile.setOwner(tile);
        destTile.setCanMoveto();
    }

    public void searchPos(int[] dest) {
        Tile destTile = tiles[dest[0]][dest[1]];
        destTile.setMoveablePosition();
    }

    public void executeMove(Move move) {
        if(move.getSourcePiece().getType() == Chess_1VS1.Type.PAWN) {
            if (move.getDestY() == move.getSourceY() - 2) {
                ((Pawn) move.getSourcePiece()).turnOffStart();
            }
            else if (move.getDestY() == move.getSourceY() - 1) {
                ((Pawn) move.getSourcePiece()).turnOffStart();
            }
        }
        tiles[move.getDestY()][move.getDestX()].updateTile(move.getSourcePiece());
        tiles[move.getSourceY()][move.getSourceX()].removePiece();
        sendCommand(getSourceCords(tiles[move.getSourceY()][move.getSourceX()]), getSourceCords(tiles[move.getDestY()][move.getDestX()]));
        disableAllMoveIndicators();
        turn = false;
        sendCommand("turn on");
        sendCommand("ZONES");
        markKingThreats();
        check();
    }

    public void executeMove(String source, String dest) {
        int[] sourceIndex = Parser.fromCordToIndex(source);
        int[] destIndex = Parser.fromCordToIndex(dest);
        Piece sourcePiece = tiles[sourceIndex[0]][sourceIndex[1]].getPiece();
        tiles[sourceIndex[0]][sourceIndex[1]].removePiece();
        tiles[destIndex[0]][destIndex[1]].updateTile(sourcePiece);
        disableAllMoveIndicators();
    }

    public void setKingZonesTeam() {
        Tile kingTile = findKingTile(team);
        King king = (King) kingTile.getPiece();
        int[] sourceIndex = getSourceIndex(kingTile);
        int[][] legalMoves = king.getLegalMoves();
        disableKingZones();
        for (int[] legalMove : legalMoves) {
            int targetY = sourceIndex[0] + legalMove[0];
            int targetX = sourceIndex[1] + legalMove[1];

            int[] targetArr = {targetY, targetX};
            if (checkIfPieceOn(targetY,targetX))
                continue;
            if (BoardClient.checkWithinBounds(targetArr)) {
                tiles[targetY][targetX].setKingzone(true);
                tiles[targetY][targetX].setOwner(kingTile);
            }
        }
    }

    public void setKingZonesEnemy() {
        Tile kingTile = findKingTile(enemyTeam);
        King king = (King) kingTile.getPiece();
        int[] sourceIndex = getSourceIndex(kingTile);
        int[][] legalMoves = king.getLegalMoves();
        disableKingZones();
        for (int[] legalMove : legalMoves) {
            int targetY = sourceIndex[0] + legalMove[0];
            int targetX = sourceIndex[1] + legalMove[1];

            int[] targetArr = {targetY, targetX};
            if (BoardClient.checkWithinBounds(targetArr)) {
                tiles[targetY][targetX].setKingzone(true);
                tiles[targetY][targetX].setOwner(kingTile);
            }
        }
    }

    public Tile findKingTile(Team kingTeam) {
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                if (t.isPieceOn() && t.getPiece().getType() == Chess_1VS1.Type.KING && t.getPiece().getSide() == kingTeam) {
                    return t;
                }
            }
        }
        return null;
    }

    private class TileMouseListener extends MouseAdapter {
        private final Tile tile;

        TileMouseListener(Tile tile) {
            this.tile = tile;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (!turn)
                return;
            Tile sourceTile = (Tile) e.getSource();
            if (!sourceTile.getPiece().isDead() && sourceTile.getPiece().getSide() == team && !sourceTile.isClicked()) {
                if (checkActivations()) {
                    disableAllActivations();
                    disableAllMoveIndicators();
                }
                sourceTile.setMoveSource(team);
                switch (sourceTile.getPiece().getType()) {
                    case PAWN, KNIGHT, KING:
                        showAvailableMoves(tile,false);
                        break;
                    case BISHOP, ROOK, QUEEN:
                        showAvailableMovesIntervall(tile,false);
                        break;
                }
            }
            else if(sourceTile.isClicked()) {
                sourceTile.setMoveSource(team);
                disableAllMoveIndicators();
            }
            else if (isValidMoveTile(sourceTile)) {
                Move move = new Move(
                        getSourceIndex(tile.getOwner()),
                        tile.getOwner().getPiece(),
                        getSourceIndex(tile)
                );
                executeMove(move);
            }
            else {
                disableAllActivations();
                disableAllMoveIndicators();
            }
        }
    }
}