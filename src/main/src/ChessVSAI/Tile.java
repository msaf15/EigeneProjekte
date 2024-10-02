package ChessVSAI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tile extends JPanel {
    private Tile owner = null;
    private Piece piece;
    private BufferedImage image;
    private BufferedImage canMoveto;
    private boolean clicked = false;
    private boolean moveablePosition = false;
    private boolean pieceOn = false;
    public Tile() {
        this.piece = new Piece();
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLayout(new GridLayout(1,1));
    }
    public void updateTile(Piece piece) {
        this.piece = piece;
        this.image = piece.getImage();
        owner = null;
        canMoveto = null;
        clicked = false;
        pieceOn = true;
        moveablePosition = false;
        rePaint();
    }
    public void removePiece() {
        this.piece = new Piece();
        this.image = null;
        this.canMoveto = null;
        clicked = false;
        moveablePosition = false;
        pieceOn = false;
        owner = null;
        rePaint();
    }
    public void setMoveSource() {
        if (piece.getType() != Type.PIECE && !clicked && piece.getSide() == Team.WHITE) {
            try {
                canMoveto = ImageIO.read(new File("src/main/resources/greencircle.png"));
                rePaint();
                clicked = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            this.canMoveto = null;
            rePaint();
            clicked = false;
        }
    }
    public void setOwner(Tile tile) {
        owner = tile;
    }
    public Tile getOwner() {
        return owner;
    }
    public void setCanMoveto() {
        try {
            canMoveto = ImageIO.read(new File("src/main/resources/greencircle.png"));
            moveablePosition = true;
            rePaint();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void removeCircle() {
        canMoveto = null;
        moveablePosition = false;
        rePaint();
    }
    public boolean isClicked() {
        return clicked;
    }
    public void setClicked() {
        clicked = !clicked;
    }
    public Piece getPiece() {
        return piece;
    }
    public void rePaint() {
        this.repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (canMoveto != null) {
            g.drawImage(canMoveto,0,0,getWidth(),getHeight(),this);
        }
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
    public boolean isMoveablePosition() {
        return moveablePosition;
    }

    public boolean isPieceOn() {
        return pieceOn;
    }

    public void setPieceOn(boolean pieceOn) {
        this.pieceOn = pieceOn;
    }
}
