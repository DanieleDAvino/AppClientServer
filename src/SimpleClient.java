import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(String[] args) {

        String hostName = "127.0.0.1";
        int port = 12345;

        try {
            Socket socket = new Socket(hostName, port);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String msg;


            while ((msg = in.readLine()) != null) {


                System.out.println(msg);

                // L'utente scrive solo se il server lo chiede, ovvero quando scrive INPUT, sennò il client deve scrivere in continuazione per forza
                if (msg.equals("INPUT:")) {
                    String risposta = scanner.nextLine();
                    out.println(risposta);
                }
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
