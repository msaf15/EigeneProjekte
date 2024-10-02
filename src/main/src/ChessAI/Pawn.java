package ChessAI;

public class Pawn extends Piece {
    private boolean start = true;
    int[][] legalMoves = {
            {-1,0},
            {-1,-1},
            {-2,0},
            {-1,1},

    };
    Pawn(Team side) {
        super(side);
    }
    @Override
    public Type getType() {
        return Type.PAWN;
    }
    public int[][] getLegalMoves() {
        return legalMoves;
    }

    public boolean isStart() {
        return start;
    }

    public void turnOffStart() {
        start = false;
    }
}
