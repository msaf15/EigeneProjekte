package Chess_1VS1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Knight extends Piece {
    Knight(Team side) {
        super(side);
    }
    private final int[][] legalMoves = {
            { 2,  1},
            { 2, -1},
            {-2,  1},
            {-2, -1},
            { 1,  2},
            { 1, -2},
            {-1,  2},
            {-1, -2},
    };
    @Override
    public BufferedImage getImage() {
        try {
            if (this.getSide() == Team.BLACK)
                return ImageIO.read(new File("main/resources/knight.png"));
            else
                return ImageIO.read(new File("main/resources/knight2.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Type getType() {
        return Type.KNIGHT;
    }
    @Override
    public int[][] getLegalMoves() {
        return legalMoves;
    }
}
