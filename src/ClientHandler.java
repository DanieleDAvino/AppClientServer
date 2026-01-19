import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    private Socket socket;//socket del client collegato a questo thread

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {

        try {
            //per leggere i messaggi del client
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            //per mandare messaggi al client
            PrintWriter out = new PrintWriter(
                    socket.getOutputStream(), true);

            //chiede username x login
            out.println("inserisci username:");
            out.println("INPUT:");
            String username = in.readLine();

            //chiede psswd x login
            out.println("inserisci password:");
            out.println("INPUT:");
            String password = in.readLine();

            //controllo login
            if (!Server.users.containsKey(username) ||
                    !Server.users.get(username).equals(password)) {
                out.println("login fallito");
                socket.close();//chiede la connessione se il login è sbagliato
                return;
            }

            out.println("login effettuato");

            out.println("SCRIVI oppure INDOVINA?");
            out.println("INPUT:");
            String ruolo = in.readLine();

           //fa scegliere al client cosa fare
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

        //gli fa sceglier eun colore
        out.println("Scegli un colore tra: ");
        out.println(Server.COLORS);
        out.println("INPUT:");

        String colore = in.readLine().trim().toLowerCase();//gli leva gli spazi all'inizio e alla fine e lo mette tutto minuscolo cosi si può conforntare con i colori salvati nel server

        Server.secretColor = colore;//salva il colore
        Server.secretHash = HashUtil.sha256(colore);//salva il colore sottoforma di hash

        out.println("Colore salvato");
        out.println("Hash colore: " + Server.secretHash);
    }

    private void indovinaColore(BufferedReader in, PrintWriter out) throws Exception {

        //controllo se qualcuno ha già scritto un colore, anche pk nn puoi indovinare un colore se nessuno prima lo ha scelto
        if (Server.secretHash == null) {
            out.println("Nessun colore impostato");
            return;
        }

        List<String> coloriRimasti = new ArrayList<>(Server.COLORS);//copia della lista dei colori per togliere quelli già provati cosi da facilitare il client
        int tentativi = 5;

        while (tentativi > 0) {

            out.println("");
            out.println("Colori disponibili: " + coloriRimasti);
            out.println("Hash corretto: " + Server.secretHash);
            out.println("Inserisci un colore");
            out.println("INPUT:");

            //lettura del tentativo del client
            String tentativo = in.readLine().trim().toLowerCase();
            String hashTentativo = HashUtil.sha256(tentativo);

            out.println("Hash inserito: " + hashTentativo);

            //confronto tra hash inserito e hash corretto
            if (hashTentativo.equals(Server.secretHash)) {
                out.println("HAI INDOVINATO!");
                return;
            }
            else
                out.println("HAI SBAGLIATO, tentativi rimasti: "+(tentativi-1));

            coloriRimasti.remove(tentativo);//rimuove il colore sbagliato
            tentativi--;//diminuisce i tentativi
        }

        //se finisce i tentativi senza indovinare
        out.println("Hai perso");
        out.println("Vuoi vedere qual è il colore (si/no)?");
        out.println("INPUT:");

        String risposta = in.readLine();

        //se il client non vuole continuare chiude
        if (!risposta.equalsIgnoreCase("si")) {
            return;
        }

        out.println("Prova con gli ultimi due colori rimasti");
        out.println("Hash corretto: " + Server.secretHash);

        //penultimo tentativo
        out.println("Inserisci il colore:");
        out.println("INPUT:");
        String penultimo = in.readLine().trim().toLowerCase();
        String hashPenultimo = HashUtil.sha256(penultimo);
        out.println("Hash inserito: " + hashPenultimo);

        if (hashPenultimo.equals(Server.secretHash)) {
            out.println("Hai indovinato!");
            return;
        }

        //ultimo tentativo
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
