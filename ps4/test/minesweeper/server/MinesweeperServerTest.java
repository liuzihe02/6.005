package minesweeper.server;

import static org.junit.Assert.*;
import java.io.*;
import java.net.Socket;
import org.junit.Test;

public class MinesweeperServerTest {
    private static final int PORT = 4444;

    /**
     * Tests that multiple clients can interact with the server simultaneously
     */
    @Test(timeout = 10000)
    public void testMultipleClients() throws Exception {
        // Start server in separate thread
        Thread server = new Thread(() -> {
            try {
                MinesweeperServer.main(new String[] {
                        "--port", String.valueOf(PORT),
                        "--size", "10,10" // Use simple 10x10 random board
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        server.start();
        Thread.sleep(100); // Give server time to start

        // Create three clients with different command sequences
        Thread client1 = createClient(new String[] {
                "dig 3 3",
                "look",
                "flag 3 4"
        });

        Thread client2 = createClient(new String[] {
                "flag 2 2",
                "deflag 2 2",
                "dig 2 2"
        });

        Thread client3 = createClient(new String[] {
                "look",
                "help",
                "dig 5 5"
        });

        // Start all clients
        client1.start();
        client2.start();
        client3.start();

        // Wait for clients to finish
        client1.join(1000);
        client2.join(1000);
        client3.join(1000);

        // Verify all clients completed
        assertFalse(client1.isAlive());
        assertFalse(client2.isAlive());
        assertFalse(client3.isAlive());
    }

    /**
     * Creates a client thread that connects to server, sends multiple commands, and disconnects
     */
    private Thread createClient(String[] commands) {
        return new Thread(() -> {
            try (Socket socket = new Socket("localhost", PORT)) {
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                // Skip welcome message
                in.readLine();

                // Send each command and read response
                for (String command : commands) {
                    out.println(command);
                    String response = in.readLine();
                    assertNotNull("Server should send response for: " + command, response);

                    // For board responses, read all lines
                    if (response.contains("-") || response.contains("F")) {
                        while (in.ready()) {
                            in.readLine();
                        }
                    }
                }

                // Disconnect
                out.println("bye");

            } catch (IOException e) {
                fail("Client connection failed: " + e.getMessage());
            }
        });
    }
}