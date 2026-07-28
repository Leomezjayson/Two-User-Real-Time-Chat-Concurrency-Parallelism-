import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * ChatClient
 * ----------
 * Connects to ChatServer and lets the user send/receive messages
 * in real time.
 *
 * Concurrency concept demonstrated: a single socket connection is
 * used for BOTH sending and receiving at the same time. This requires
 * two threads:
 *   - The main thread reads what the user types and sends it.
 *   - A separate "listener" thread continuously reads incoming
 *     messages from the server and prints them.
 * Without a second thread, the client would have to choose between
 * waiting for user input OR waiting for incoming messages — it could
 * not do both at once, so incoming messages would be delayed until
 * the user pressed Enter.
 */
public class ChatClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to chat server at "
                    + SERVER_HOST + ":" + SERVER_PORT);
            System.out.println("Type a message and press Enter. Type 'exit' to quit.\n");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // Listener thread: runs concurrently with the main thread,
            // continuously waiting for messages from the server and
            // printing them the instant they arrive.
            Thread listenerThread = new Thread(() -> {
                try {
                    String incoming;
                    while ((incoming = in.readLine()) != null) {
                        System.out.println("\nPeer: " + incoming);
                        System.out.print("You: ");
                    }
                } catch (IOException e) {
                    System.out.println("\nConnection to server lost.");
                }
            });
            // Daemon thread: won't prevent the program from exiting
            // once the main thread finishes.
            listenerThread.setDaemon(true);
            listenerThread.start();

            // Main thread: handles sending. Runs independently of the
            // listener thread above — this is the concurrency in action.
            while (true) {
                System.out.print("You: ");
                String message = scanner.nextLine();

                if (message.equalsIgnoreCase("exit")) {
                    System.out.println("Closing connection.");
                    break;
                }
                out.println(message);
            }

        } catch (IOException e) {
            System.err.println("Client error: " + e.getMessage());
        }
    }
}
