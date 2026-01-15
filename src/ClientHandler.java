import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            out.println("SCRIVI oppure INDOVINA?");
            out.println("INPUT:");
            String ruolo = in.readLine();

            if (ruolo.equalsIgnoreCase("SCRIVI")) {
                scriviColore(in, out);
            } else if (ruolo.equalsIgnoreCase("INDOVINA")) {
                indovinaColore(in, out);
            } else {
                out.println("Ruolo non valido");
            }

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void scriviColore(BufferedReader in, PrintWriter out) throws Exception {

        out.println("Scegli un colore tra: ");
        out.println(Server.COLORS);
        out.println("INPUT:");

        String colore = in.readLine().trim().toLowerCase();

        Server.secretColor = colore;
        Server.secretHash = HashUtil.sha256(colore);

        out.println("Colore salvato");
        out.println("Hash colore: " + Server.secretHash);
    }

    private void indovinaColore(BufferedReader in, PrintWriter out) throws Exception {

        if (Server.secretHash == null) {
            out.println("Nessun colore impostato");
            return;
        }

        List<String> coloriRimasti = new ArrayList<>(Server.COLORS);
        int tentativi = 5;

        while (tentativi > 0) {

            out.println("");
            out.println("Colori disponibili: " + coloriRimasti);
            out.println("Hash corretto: " + Server.secretHash);
            out.println("Inserisci un colore");
            out.println("INPUT:");

            String tentativo = in.readLine().trim().toLowerCase();
            String hashTentativo = HashUtil.sha256(tentativo);

            out.println("Hash inserito: " + hashTentativo);

            if (hashTentativo.equals(Server.secretHash)) {
                out.println("HAI INDOVINATO!");
                return;
            }
            else
                out.println("HAI SBAGLIATO, tentativi rimasti: "+(tentativi-1));

            coloriRimasti.remove(tentativo);
            tentativi--;
        }

        out.println("Hai perso");
        out.println("Vuoi vedere qual è il colore (si/no)?");
        out.println("INPUT:");

        String risposta = in.readLine();

        if (!risposta.equalsIgnoreCase("si")) {
            return;
        }

        out.println("Prova con gli ultimi due colori rimasti");
        out.println("Hash corretto: " + Server.secretHash);

        // penultimo tentativo
        out.println("Inserisci il colore:");
        out.println("INPUT:");
        String penultimo = in.readLine().trim().toLowerCase();
        String hashPenultimo = HashUtil.sha256(penultimo);
        out.println("Hash inserito: " + hashPenultimo);

        if (hashPenultimo.equals(Server.secretHash)) {
            out.println("Hai indovinato!");
            return;
        }

        // ultimo tentativo
        coloriRimasti.remove(penultimo);
        out.println("Inserisci l'ultimo colore rimasto:");
        out.println("INPUT:");
        String ultimo = in.readLine().trim().toLowerCase();
        String hashUltimo = HashUtil.sha256(ultimo);
        out.println("Hash inserito: " + hashUltimo);

        if (hashUltimo.equals(Server.secretHash)) {
            out.println("Indovinato all'ultimo tentativo");
        } else {
            out.println("Colore non indovinato");
        }
    }
}
