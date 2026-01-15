import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static final List<String> COLORS =
            Arrays.asList("rosso", "arancione", "giallo", "verde", "blu", "indaco", "viola");

    public static String secretColor = null;
    public static String secretHash = null;

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server avviato");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new ClientHandler(socket)).start();
        }
    }
}
