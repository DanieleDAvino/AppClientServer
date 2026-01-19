import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    //lista colori
    public static final List<String> COLORS =
            Arrays.asList("rosso", "arancione", "giallo", "verde", "blu", "indaco", "viola");

    public static String secretColor = null;//colore scelto dall'utente
    public static String secretHash = null;//colore scelto dall'utente ma in formato hash


    //lista degli utenti che possono loggare
    public static Map<String, String> users = new HashMap<>();
    static {
        users.put("AlessioBillorosso", "AB0704");
        users.put("DanieleDAvino", "DD1008");
        users.put("GiulioPrelati", "GP2701");
        users.put("JordanSquarta", "JS2602");
    }

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(12345);//crea il server sulla porta 12345
        System.out.println("Server avviato");

        while (true) {//il server resta in ascolto di nuove connessioni
            Socket socket = serverSocket.accept();//accetta un client
            System.out.println("Nuovo client connesso: " + socket.getInetAddress());
            new Thread(new ClientHandler(socket)).start();//ogni client viene gestito da un thread
        }
    }
}
