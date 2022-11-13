import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    protected static String HOST = "127.0.0.1";
    protected static int PORT = 8989;

    public static void main(String[] args) {
        try (Socket clientSocket = new Socket(HOST, PORT);
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            System.out.println("Соединение установлено");
            out.println("бизнес");
            String data = in.readLine();
            System.out.println(data);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
