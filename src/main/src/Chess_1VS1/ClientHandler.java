package Chess_1VS1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Socket socket;
    private Server server;
    private int id;
    private BufferedReader in;
    private PrintWriter out;
    ClientHandler(Socket socket, Server server, int id) {
        try {
            this.socket = socket;
            this.server = server;
            this.id = id;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            String ans;
            while ((ans = in.readLine()) != null) {
                System.out.println(ans);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String command) {
        out.println(command);
    }
}
