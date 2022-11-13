import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class Main {
    public static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pcs-final-diplom\\pdfs"));
        System.out.println(engine.search("бизнес"));

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream());
                ) {
                    String word = in.readLine();
                    List<PageEntry> list = engine.search(word);
                    GsonBuilder builder = new GsonBuilder();
                    Gson gson = builder.create();
                    String data = gson.toJson(list.get(0), PageEntry.class);
                    out.println(data);
                }
            }
        }
    }
}