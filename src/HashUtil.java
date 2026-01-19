import java.security.MessageDigest;

public class HashUtil {

    // metodo per calcolare l'hash
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));//trasforma la stringa in byte e calcola l'hash

            String result = "";
            for (byte b : hash) {
                result += String.format("%02x", b);
            }//ogni byte dell’hash diventa 2 caratteri esadecimali e tutte queste coppie vengono concatenate
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
