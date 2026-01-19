import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SimpleClient {

    public static void main(String[] args) {

        String hostName = "127.0.0.1";//indirizzo del server
        int port = 12345;//porta su cui il server è in ascolto

        try {
            Socket socket = new Socket(hostName, port);//creazione del socket per collegarsi al server

            //x leggere i messaggi che arrivano dal server
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            //x inviare messaggi al server
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            String msg;


            while ((msg = in.readLine()) != null) { //legge tutti i messaggi inviati dal server


                System.out.println(msg); //stampa quello che dice il server

                //l'utente scrive solo se il server lo chiede, ovvero quando scrive INPUT, sennò il client deve scrivere in continuazione per forza
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
