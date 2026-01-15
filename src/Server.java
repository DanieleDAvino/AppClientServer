import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public static final List<String> COLORS =
            Arrays.asList("rosso", "arancione", "giallo", "verde", "blu", "indaco", "viola");

    public static String secretColor = null;
    public static String secretHash = null;


    public static Map<String, String> users = new HashMap<>();
    static {
        users.put("AlessioBillorosso", "AB0704");
        users.put("DanieleDAvino", "DD1008");
        users.put("GiulioPrelati", "GP2701");
        users.put("JordanSquarta", "JS2602");
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Server avviato");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Nuovo client connesso: " + socket.getInetAddress());
            new Thread(new ClientHandler(socket)).start();
        }
    }
}
