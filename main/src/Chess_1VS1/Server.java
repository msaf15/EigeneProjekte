package Chess_1VS1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Server {
    private List<ClientHandler> clientHandlerList;
    public Server(int port) {
        clientHandlerList = new ArrayList<>();
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running");
            Socket p1 = serverSocket.accept();
            System.out.println("p1 has connected");

            Socket p2 = serverSocket.accept();
            System.out.println("p2 has connected");

            ClientHandler clientHandlerP1 = new ClientHandler(p1, this);
            ClientHandler clientHandlerP2 = new ClientHandler(p2, this);
            clientHandlerList.add(clientHandlerP1);
            clientHandlerList.add(clientHandlerP2);
            clientHandlerP1.start();
            clientHandlerP2.start();
            Random random = new Random();
            int ranNum = random.nextInt(0,2);
            PrintWriter outP1 = new PrintWriter(p1.getOutputStream(),true);
            PrintWriter outP2 = new PrintWriter(p2.getOutputStream(),true);
            outP1.println("turn on");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void singleCast(String command, ClientHandler clientHandler) {
        for (ClientHandler ch : clientHandlerList) {
            if (!(ch == clientHandler))
                ch.sendMessage(command);
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() -> new Server(6000));
        thread.start();
    }
}
