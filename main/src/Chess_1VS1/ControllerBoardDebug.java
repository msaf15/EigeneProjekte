package Chess_1VS1;

import javax.swing.*;

public class ControllerBoardDebug {
    public static void main(String[] args) {
        new Thread(() -> new Server(6000)).start();
        SwingUtilities.invokeLater(() -> new BoardClient(Team.WHITE));
        SwingUtilities.invokeLater(() -> new BoardClient(Team.BLACK));
    }
}
