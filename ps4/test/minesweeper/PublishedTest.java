/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import org.junit.Test;

import minesweeper.server.MinesweeperServer;

/**
 * Tests basic LOOK and DIG commands and X,Y directions.
 * 
 * for single client thread only
 */
public class PublishedTest {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 4000 + new Random().nextInt(1 << 15);

    private static final int MAX_CONNECTION_ATTEMPTS = 10;

    private static final String BOARDS_PKG = "minesweeper/boards/";

    /**
     * Start a MinesweeperServer in debug mode with a board file from BOARDS_PKG.
     * @param boardFile board to load
     * @return thread running the server
     * @throws IOException if the board file cannot be found
     */
    public static Thread startMinesweeperServer(String boardFile) throws IOException {
        final URL boardURL = ClassLoader.getSystemClassLoader().getResource(BOARDS_PKG + boardFile);
        if (boardURL == null) {
            throw new IOException("Failed to locate resource " + boardFile);
        }
        final String boardPath;
        try {
            boardPath = new File(boardURL.toURI()).getAbsolutePath();
        } catch (URISyntaxException urise) {
            throw new IOException("Invalid URL " + boardURL, urise);
        }

        //we decide the args here!
        final String[] args = new String[] {
                "--debug", // in debug mode
                "--port", Integer.toString(PORT), //uses the random PORT defined at the start, NOT the default port!
                "--file", boardPath
        };

        //the random port is passed in as an arg
        Thread serverThread = new Thread(() -> MinesweeperServer.main(args));
        serverThread.start();
        return serverThread;
    }

    /**
     * Create and client socket; Connect to a MinesweeperServer and return the connected socket.
     * 
     * 
     * @param server abort connection attempts if the server thread dies. takes in the server thread
     * @return client socket connected to the serverSocket
     * @throws IOException if the connection fails
     */
    public static Socket connectToMinesweeperServer(Thread server) throws IOException {
        int attempts = 0;
        while (true) {
            try {
                // the port will start a SINGLE new client thread
                Socket clientSocket = new Socket(LOCALHOST, PORT);
                clientSocket.setSoTimeout(3000);
                return clientSocket;
            } catch (ConnectException ce) {
                //check if the server thread is fine
                if (!server.isAlive()) {
                    throw new IOException("Server thread not running");
                }
                if (++attempts > MAX_CONNECTION_ATTEMPTS) {
                    throw new IOException("Exceeded max connection attempts", ce);
                }
                try {
                    Thread.sleep(attempts * 10);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    @Test(timeout = 10000000)
    public void publishedTest() throws IOException {

        //create 
        Thread thread = startMinesweeperServer("board_file_5");

        //client socket, using a random port
        Socket clientSocket = connectToMinesweeperServer(thread);

        //receive stuff in from the server
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //autoflush is true; everytime you use println buffer is always flushed
        // send stuff from the client out to the server
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

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

        out.println("dig 3 1");
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - 1 - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());
        assertEquals("- - - - - - -", in.readLine());

        out.println("dig 4 1");
        assertEquals("BOOM!", in.readLine());

        out.println("look"); // debug mode is on
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("             ", in.readLine());
        assertEquals("1 1          ", in.readLine());
        assertEquals("- 1          ", in.readLine());

        out.println("bye");
        clientSocket.close();
    }
}