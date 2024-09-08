import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Client
 */
public class Client {
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter out;

    public Client() {
        try {
            System.out.println("Sending request to the server..");
            socket = new Socket("127.0.0.1", 7777);
            System.out.println("Connection done.");

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startReading() {

        // thread that keeps reading the date

        Runnable r1 = () -> {
            System.out.println("Reader Started... ");

            try {
                while (true) {
                    String msg = br.readLine();

                    if (msg.equals("exit")) {
                        System.out.println("Server terminated the chat! ");
                        break;
                    }
                    System.out.println("Server: " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        };
        new Thread(r1).start();
    }

    public void startWriting() {
        System.out.println("Writer Started...");
        Runnable r2 = () -> {
            while (true) {
                try {
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();
                    // System.out.println("Client: " + content);
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
        new Client();
    }
}