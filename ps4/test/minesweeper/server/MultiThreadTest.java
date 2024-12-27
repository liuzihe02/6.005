package minesweeper.server;

import static org.junit.Assert.*;
import java.io.*;
import java.net.Socket;
import java.util.Random;
import org.junit.Test;

import minesweeper.PublishedTest;

/**
 * Tests concurrent operations with multiple clients.
 */
public class MultiThreadTest {

    @Test(timeout = 10000)
    public void testConcurrentClients() throws IOException, InterruptedException {

        // Start server
        Thread serverThread = PublishedTest.startMinesweeperServer("board_file_5");

        // Create first client that flags a position
        Thread client1 = new Thread(() -> {
            try {
                Socket clientSocket = PublishedTest.connectToMinesweeperServer(serverThread);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Skip welcome message
                assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

                //send some output to the client
                out.println("look");
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());
                assertEquals("- - - - - - -", in.readLine());

                // Keep connection alive briefly
                Thread.sleep(100);

                out.println("look"); // debug mode is on
                assertEquals("             ", in.readLine());
                assertEquals("             ", in.readLine());
                assertEquals("             ", in.readLine());
                assertEquals("             ", in.readLine());
                assertEquals("             ", in.readLine());
                assertEquals("1 1          ", in.readLine());
                assertEquals("- 1          ", in.readLine());

                clientSocket.close();
            } catch (Exception e) {
                fail("Client 1 failed: " + e.getMessage());
            }
        });

        // Create second client that looks at the board
        Thread client2 = new Thread(() -> {
            try {
                Socket clientSocket = PublishedTest.connectToMinesweeperServer(serverThread);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Skip welcome message
                assertTrue("expected HELLO message", in.readLine().startsWith("Welcome"));

                // Wait briefly to ensure first client has done its action
                Thread.sleep(50);

                //sends the boom message
                out.println("dig 4 1");
                assertEquals("BOOM!", in.readLine());

                clientSocket.close();
            } catch (Exception e) {
                fail("Client 2 failed: " + e.getMessage());
            }
        });

        // Start both clients and wait for completion
        client1.start();
        client2.start();
        client1.join();
        client2.join();
    }
}