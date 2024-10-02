package ChessV1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Piece {
    private Team side;
    private boolean dead;
    Piece(Team side) {
        this.side = side;
        dead = false;
    }
    Piece() {
        dead = true;
    }
    public BufferedImage getImage() {
        try {
            if (side == Team.BLACK)
                return ImageIO.read(new File("src/main/resources/pawn.png"));
            else
                return ImageIO.read(new File("src/main/resources/pawn2.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean isDead() {
        return dead;
    }
    public int[][] getLegalMoves() {
        return new int[][]{};
    }
    public Team getSide() {
        return side;
    }
    public Type getType() {
        return Type.PIECE;
    }
}
