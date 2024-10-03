package ChessVSAI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bishop extends Piece {
    private final int[][] legalMoves = {
            {1,-1}, {-1,-1},
            {1,1}, {-1,1},
    };
    Bishop(Team side) {
        super(side);
    }
    @Override
    public BufferedImage getImage() {
        try {
            if (this.getSide() == Team.BLACK)
                return ImageIO.read(new File("main/resources/bishop.png"));
            else
                return ImageIO.read(new File("main/resources/bishop2.png"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Type getType() {
        return Type.BISHOP;
    }

    @Override
    public int[][] getLegalMoves() {
        return legalMoves;
    }
}
