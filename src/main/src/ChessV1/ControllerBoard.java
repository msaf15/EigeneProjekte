package ChessV1;

import javax.swing.*;

public class  ControllerBoard {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Board::new);
    }
}
