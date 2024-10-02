package ChessAI;

public class Test {
    public static void main(String[] args) {
        Tile[][] t = new Tile[1][1];
        Tile s = new Tile();
        t[0][0] = s;
        if (t[0][0].equals(s)) {
            System.out.println("its true");
        }
    }
}
