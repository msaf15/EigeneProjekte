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
    private List<ClientHandler> clientHandlerList;
    public Server(int port) {
        clientHandlerList = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server running");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlerList.add(clientHandler);
                clientHandler.start();
            }
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
