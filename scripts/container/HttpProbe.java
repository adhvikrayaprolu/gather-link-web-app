import java.net.HttpURLConnection;
import java.net.URI;

/** Small runtime probe: the JRE image needs no curl or development tools. */
public final class HttpProbe {
    public static void main(String[] args) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) URI.create(args[0]).toURL().openConnection();
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            connection.setInstanceFollowRedirects(false);
            if (connection.getResponseCode() != 200) {
                System.exit(1);
            }
        } catch (Exception unavailable) {
            System.exit(1);
        } finally {
            if (connection != null) connection.disconnect();
        }
    }
}
