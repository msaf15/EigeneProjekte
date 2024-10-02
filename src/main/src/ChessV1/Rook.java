package ChessV1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Rook extends Piece {
    private final int[][] legalMoves = {
            {1,0},
            {-1,0},
            {0,1},
            {0,-1},
    };
    Rook(Team side) {
        super(side);
    }
    @Override
    public BufferedImage getImage() {
        try {
            if (this.getSide() == Team.BLACK)
                return ImageIO.read(new File("src/main/resources/rook.png"));
            else
                return ImageIO.read(new File("src/main/resources/rook2.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int[][] getLegalMoves() {
        return legalMoves;
    }

    @Override
    public Type getType() {
        return Type.ROOK;
    }
}
