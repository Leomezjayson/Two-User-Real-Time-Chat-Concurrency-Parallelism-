import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ChatServer
 * ----------
 * Accepts exactly two clients and relays every message one sends
 * to the other, in real time.
 *
 * Concurrency concept demonstrated: the server handles both clients
 * SIMULTANEOUSLY using one thread per client (ClientHandler). Without
 * threads, the server could only read from one client at a time and
 * would block waiting on it, freezing the other client's messages.
 */
public class ChatServer {

    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("========================================");
            System.out.println("  2-User Chat Server");
            System.out.println("  Waiting for 2 clients on port " + PORT);
            System.out.println("========================================");

            // Accept exactly two clients before starting the relay.
            Socket clientSocketA = serverSocket.accept();
            System.out.println("Client A connected: " + clientSocketA.getRemoteSocketAddress());

            Socket clientSocketB = serverSocket.accept();
            System.out.println("Client B connected: " + clientSocketB.getRemoteSocketAddress());
            System.out.println("Both clients connected — starting relay.\n");

            ClientHandler handlerA = new ClientHandler(clientSocketA, "Client A");
            ClientHandler handlerB = new ClientHandler(clientSocketB, "Client B");

            // Each handler needs to know about the other, so it can
            // forward messages to its peer.
            handlerA.setPeer(handlerB);
            handlerB.setPeer(handlerA);

            // Starting both as separate threads is what lets them run
            // CONCURRENTLY — each blocks independently on its own
            // client's input, without holding up the other.
            Thread threadA = new Thread(handlerA);
            Thread threadB = new Thread(handlerB);
            threadA.start();
            threadB.start();

            // Wait for both to finish (i.e. both clients disconnected)
            // before the server process exits.
            threadA.join();
            threadB.join();

            System.out.println("Both clients disconnected. Server shutting down.");

        } catch (IOException | InterruptedException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    /**
     * Handles one client's connection on its own thread: reads
     * incoming lines from that client and forwards them to the peer.
     */
    static class ClientHandler implements Runnable {
        private final Socket socket;
        private final String label;
        private ClientHandler peer;
        private PrintWriter out;

        ClientHandler(Socket socket, String label) {
            this.socket = socket;
            this.label = label;
        }

        void setPeer(ClientHandler peer) {
            this.peer = peer;
        }

        /**
         * Called by the peer handler to push a message to THIS client.
         */
        void sendToClient(String message) {
            if (out != null) {
                out.println(message);
            }
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()))) {

                out = new PrintWriter(socket.getOutputStream(), true);

                String line;
                // Blocks here waiting for a line from THIS client —
                // but since this runs on its own thread, it doesn't
                // block the other client's handler thread at all.
                while ((line = in.readLine()) != null) {
                    System.out.println(label + " -> " + line);
                    if (peer != null) {
                        peer.sendToClient(line);
                    }
                }
            } catch (IOException e) {
                System.out.println(label + " disconnected.");
            } finally {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
