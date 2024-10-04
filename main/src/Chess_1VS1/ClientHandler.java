package Chess_1VS1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler extends Thread {
    private Server server;
    private BufferedReader in;
    private PrintWriter out;
    ClientHandler(Socket socket, Server server) {
        try {
            this.server = server;
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        }
        catch (IOException e) {
            System.err.println("IO-Exception: " + e.getMessage());
        }
    }
    @Override
    public void run() {
        try {
            String ans;
            while ((ans = in.readLine()) != null) {
                server.singleCast(ans,this);
            }
        }
        catch (IOException e) {
            System.err.println("IO-Exception: " + e.getMessage());
        }
        finally {
            try {
                in.close();
                out.close();
            }
            catch (IOException e) {
                System.err.println("IO-Exception: " + e.getMessage());
            }
        }
    }

    public void sendMessage(String command) {
        out.println(command);
        out.flush();
    }
}
