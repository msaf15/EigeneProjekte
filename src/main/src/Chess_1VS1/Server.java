package Chess_1VS1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private ServerSocket serverSocket;
    private int port;
    private List<ClientHandler> clientHandlerList;
    public Server(int port) {
        this.port = port;
        clientHandlerList = new ArrayList<>();
        try {
            int num = 0;
            serverSocket = new ServerSocket(port);
            System.out.println("Server running");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this,num++);
                clientHandlerList.add(clientHandler);
                clientHandler.start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void singleCast(int id, String command) {
        clientHandlerList.get(id).sendMessage(command);
    }

    public static void main(String[] args) {
        Thread thread = new Thread(() -> new Server(6000));
        thread.start();
    }
}
