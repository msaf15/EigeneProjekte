package Chess_1VS1;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class King extends Piece {
    private final int[][] legalMoves = {
            {1,-1}, {-1,-1},
            {1,1}, {-1,1},
            {1,0}, {-1,0},
            {0,1}, {0,-1},
    };
    King(Team side) {
        super(side);
    }
    @Override
    public BufferedImage getImage() {
        try {
            if (this.getSide() == Team.BLACK)
                return ImageIO.read(new File("src/main/resources/king.png"));
            else
                return ImageIO.read(new File("src/main/resources/king2.png"));
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
        return Type.KING;
    }
}
