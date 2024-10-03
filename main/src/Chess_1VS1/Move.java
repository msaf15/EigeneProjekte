package Chess_1VS1;

public class Move {
    private final int sourceY;
    private final int sourceX;
    private final int destX;
    private final int destY;

    private final Piece sourcePiece;
    Move(int[] source, Piece sourcePiece, int[] dest) {
        sourceY = source[0];
        sourceX = source[1];
        destY = dest[0];
        destX = dest[1];
        this.sourcePiece = sourcePiece;
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
