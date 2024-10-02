package ChessVSAI;

public class Move {
    private final int sourceY;
    private final int sourceX;
    private final int destX;
    private final int destY;
    // private final int moveY;
    // private final int moveX;
    private final Piece sourcePiece;
    private final Piece destPiece;
    private Type sourceType;
    private Type destType;
    private Tile[][] gameState;
    Move(int[] source, Piece sourcePiece, int[] dest, Piece destPiece, Tile[][] gameState) {
        sourceY = source[0];
        sourceX = source[1];
        destY = dest[0];
        destX = dest[1];
        // moveY = move[0];
        // moveX = move[1];
        this.sourcePiece = sourcePiece;
        this.destPiece = destPiece;
        if (sourcePiece != null)
            sourceType = sourcePiece.getType();
        if (destPiece != null)
            destType = destPiece.getType();
        this.gameState = gameState;
    }
    public int getSourceY() {
        return sourceY;
    }

    public int getSourceX() {
        return sourceX;
    }

    public Piece getSourcePiece() {
        return sourcePiece;
    }

    public int getDestX() {
        return destX;
    }

    public int getDestY() {
        return destY;
    }

}
