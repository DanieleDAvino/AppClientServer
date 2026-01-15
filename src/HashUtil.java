import java.security.MessageDigest;

public class HashUtil {

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));

            String result = "";
            for (byte b : hash) {
                result += String.format("%02x", b);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}
