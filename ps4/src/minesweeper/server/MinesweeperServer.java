/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper.server;

import java.io.*;
import java.net.*;
import java.util.*;

import minesweeper.Board;

/**
 * Multiplayer Minesweeper server.
 * 
 */
public class MinesweeperServer {

    // System thread safety argument
    // 1. The Board class is thread-safe as all its mutating methods are synchronized on a private lock
    // 2. The static board variable is only assigned once during server startup
    // 3. The static players variable is modified using synchronized blocks
    // 4. Each client connection is handled in its own thread with its own local variables
    // 5. The only shared state between threads is the board and players count, which are properly synchronized

    /** Default server port. */
    private static final int DEFAULT_PORT = 4444;
    /** Maximum port number as defined by ServerSocket. */
    private static final int MAXIMUM_PORT = 65535;
    /** Default square board size. */
    private static final int DEFAULT_SIZE = 5;

    /** Socket for receiving incoming connections. */
    private final ServerSocket serverSocket;
    /** True if the server should *not* disconnect a client after a BOOM message. */
    private final boolean debug;

    //this is a static board field - currently only a single board available for all servers
    // in the future, may include support for multiple boards available

    // cannot be final here because we may want to reassign the board later
    // good for mutable shared state
    // VERY IMPORTANT: CURRENTLY BOARD IS A STATIC VARIABLE
    // we only allow one board and one game session at any time here
    private static Board board;
    /** Number of players currectly connected. */
    private static int players;

    // Abstraction function:
    //   static variables and methods represent a minesweeper server with a board
    //   instance variables and methods represent a thread serving a client user
    // Rep invariant:
    //   none
    // Safety from rep exposure:
    //   board and players are never returned, and are declared as private

    /**
     * Make a MinesweeperServer that listens for connections on port.
     * 
     * @param port port number, requires 0 <= port <= 65535
     * @param debug debug mode flag
     * @throws IOException if an error occurs opening the server socket
     */
    public MinesweeperServer(int port, boolean debug) throws IOException {
        //Java class
        this.serverSocket = new ServerSocket(port);
        this.debug = debug;
        //try starting the server
        serve();
    }

    /**
     * Run the server, listening for client connections and handling them.
     * Never returns unless an exception is thrown.
     * 
     * Game continues endlessly! There is no win condition
     * 
     * @throws IOException if the main server socket is broken
     *                     (IOExceptions from individual clients do *not* terminate serve())
     * 
     * 
     * 
    TIMELINE:
    
    Main Thread          Client Thread 1        Client Thread 2
    -------------        --------------        --------------
    accept() -> wait
    client1 connects
    spawn Thread1        START
    accept() -> wait     handle client1...
    client2 connects     still running...
    spawn Thread2        still running...      START
    accept() -> wait     still running...      handle client2...
                        client1 disconnects
                        END                    still running...
                                                still running...
                                                client2 disconnects
                                                END
     * 
     * 
     * 
     */
    public void serve() throws IOException {
        while (true) {
            // block until a client connects
            /*
             * For a complete socket conneciton, we have THREE sockets
             * The Server Socket (or Listener Socket) - The ServerSocket that listens for connections
            The Accept Socket (or Server-side Connection Socket) - The Socket created by accept()
            The Client Socket (or Client-side Connection Socket) - The Socket created by the client
            
            so the below code accepts multiple new clients, creates multiple acceptSocket
             * 
             */
            Socket acceptSocket = serverSocket.accept();

            //create a new thread for each client
            new Thread(() -> {
                // handle the client
                try {
                    try {
                        handleConnection(acceptSocket);
                    }
                    // if anything wrong, close related accepted server socket
                    finally {
                        acceptSocket.close();
                    }
                    //handle the exception outside
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }

                //after starting a thread, immediately goes to next loop
            }).start();

        }
    }

    /**
     * Handle a single client connection. Returns when client disconnects.
     * 
     * @param acceptSocket socket where the client is connected
     * @throws IOException if the connection encounters an error or terminates unexpectedly
     */
    private void handleConnection(Socket acceptSocket) throws IOException {
        // Create a reader to receive input from the client
        BufferedReader in = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
        // Create a writer to send output to the client
        // The 'true' parameter enables auto-flushing - output is sent immediately
        PrintWriter out = new PrintWriter(acceptSocket.getOutputStream(), true);

        try {
            // Send HELLO message immediately upon connection
            updatePlayers(1);
            String helloMsg = String.format(
                    "Welcome to Minesweeper. Board: %d columns by %d rows. Players: %d including you. Type 'help' for help.",
                    board.width, board.height, players);
            out.println(helloMsg);

            // Start a loop that continues as long as we receive input
            // first time store in.readLine()
            // terminate when line is null
            // read the next line. will be null when client disconnects
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                //process and get the output message
                String output = handleRequest(line);
                if (output != null) {
                    // Send the response back to the client
                    out.println(output);

                    //check if player wants to leave
                    if (output.endsWith("Goodbye!")) { // Check for disconnect signal
                        break; // Exit loop and close connection
                    }

                    // if not in debug mode and boom then must close connection'
                    if (output.endsWith("BOOM!") && !debug) { // Check for disconnect signal
                        break; // Exit loop and close connection
                    }
                }
            }
        } finally {
            // When the connection ends (either normally or due to an error),
            // closing the socket is equivalent to closing the input and output streams!
            acceptSocket.close();
        }
    }

    /**
     * Handler for client input, performing requested operations and returning an output message.
     * 
     * @param input message from client
     * @return message to client, or null if none
     */
    private String handleRequest(String input) {
        String regex = "(look)|(help)|(bye)|"
                + "(dig -?\\d+ -?\\d+)|(flag -?\\d+ -?\\d+)|(deflag -?\\d+ -?\\d+)";
        if (!input.matches(regex)) {
            // Invalid input - return help message
            return "Commands: look | dig x y | flag x y | deflag x y | help | bye";
        }
        String[] tokens = input.split(" ");

        // 'look' request
        if (tokens[0].equals("look")) {
            return board.toString();
        } else if (tokens[0].equals("help")) {
            // 'help' request
            return "Commands: look | dig x y | flag x y | deflag x y | help | bye";
        } else if (tokens[0].equals("bye")) {
            // 'bye' request
            updatePlayers(-1);
            //TODO: need to terminate connection here
            return "You chose to leave the game. Goodbye!";
        } else {
            int x = Integer.parseInt(tokens[1]);
            int y = Integer.parseInt(tokens[2]);
            if (tokens[0].equals("dig")) {
                // 'dig x y' request
                boolean hitBomb = board.dig(x, y);
                //bomb went off
                if (hitBomb) {
                    return "BOOM!";
                }
                //no bomb
                return board.toString();
            } else if (tokens[0].equals("flag")) {
                // 'flag x y' request
                board.flag(x, y);
                return board.toString();
            } else if (tokens[0].equals("deflag")) {
                // 'deflag x y' request
                board.deflag(x, y);
                return board.toString();
            }
        }
        // TODO: Should never get here, make sure to return in each of the cases above
        throw new UnsupportedOperationException();
    }

    /**
    * Creates a random board with given dimensions.
    */
    private static Board createRandomBoard(int sizeX, int sizeY) {
        // we use ArrayList instead of int[][] so we allow it to grow dynamically
        List<int[]> mines = new ArrayList<>();
        Random random = new Random();

        // For each square, 25% chance of being a mine
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                if (random.nextDouble() < 0.25) {
                    mines.add(new int[] { x, y });
                }
            }
        }
        //convert to regular array
        // new int[0][] is the type we want to convert to
        // Java will actually ignore the zero and convert to correct size
        // this is equivalent to int[mines.size()][]
        return new Board(sizeX, sizeY, mines.toArray(new int[0][]));
    }

    /**
    * Loads a board from a file.
    */
    private static Board loadBoardFromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read board dimensions

            //read the first line
            String[] dimensions = reader.readLine().split(" ");
            //x-coord
            int width = Integer.parseInt(dimensions[0]);
            //y-coord
            int height = Integer.parseInt(dimensions[1]);

            List<int[]> mines = new ArrayList<>();

            // Read each line and find mines

            // for each row
            for (int y = 0; y < height; y++) {
                String[] values = reader.readLine().split(" ");
                // for each elem along the wrong
                for (int x = 0; x < width; x++) {
                    if (values[x].equals("1")) {
                        mines.add(new int[] { x, y });
                    }
                }
            }

            //convert to regular array
            return new Board(width, height, mines.toArray(new int[0][]));
        } catch (Exception e) {
            throw new RuntimeException("Invalid board file format", e);
        }
    }

    /**
     * Start a MinesweeperServer using the given arguments.
     * 
     * <br> Usage:
     *      MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]
     * 
     * <br> The --debug argument means the server should run in debug mode. The server should disconnect a
     *      client after a BOOM message if and only if the --debug flag was NOT given.
     *      Using --no-debug is the same as using no flag at all.
     * <br> E.g. "MinesweeperServer --debug" starts the server in debug mode.
     * 
     * <br> PORT is an optional integer in the range 0 to 65535 inclusive, specifying the port the server
     *      should be listening on for incoming connections.
     * <br> E.g. "MinesweeperServer --port 1234" starts the server listening on port 1234.
     * 
     * <br> SIZE_X and SIZE_Y are optional positive integer arguments, specifying that a random board of size
     *      SIZE_X*SIZE_Y should be generated.
     * <br> E.g. "MinesweeperServer --size 42,58" starts the server initialized with a random board of size
     *      42*58.
     * 
     * <br> FILE is an optional argument specifying a file pathname where a board has been stored. If this
     *      argument is given, the stored board should be loaded as the starting board.
     * <br> E.g. "MinesweeperServer --file boardfile.txt" starts the server initialized with the board stored
     *      in boardfile.txt.
     * 
     * <br> The board file format, for use with the "--file" option, is specified by the following grammar:
     * <pre>
     *   FILE ::= BOARD LINE+
     *   BOARD ::= X SPACE Y NEWLINE
     *   LINE ::= (VAL SPACE)* VAL NEWLINE
     *   VAL ::= 0 | 1
     *   X ::= INT
     *   Y ::= INT
     *   SPACE ::= " "
     *   NEWLINE ::= "\n" | "\r" "\n"?
     *   INT ::= [0-9]+
     * </pre>
     * 
     * <br> If neither --file nor --size is given, generate a random board of size 10x10.
     * 
     * <br> Note that --file and --size may not be specified simultaneously.
     * 
     * @param args arguments as described
     */
    public static void main(String[] args) {
        // Command-line argument parsing is provided. Do not change this method.
        boolean debug = false;
        int port = DEFAULT_PORT;
        //sizeX and sizeY are already initialized to default size!
        int sizeX = DEFAULT_SIZE;
        int sizeY = DEFAULT_SIZE;
        Optional<File> file = Optional.empty();

        Queue<String> arguments = new LinkedList<String>(Arrays.asList(args));
        try {
            while (!arguments.isEmpty()) {
                String flag = arguments.remove();
                try {
                    if (flag.equals("--debug")) {
                        debug = true;
                    } else if (flag.equals("--no-debug")) {
                        debug = false;
                    } else if (flag.equals("--port")) {
                        port = Integer.parseInt(arguments.remove());
                        if (port < 0 || port > MAXIMUM_PORT) {
                            throw new IllegalArgumentException("port " + port + " out of range");
                        }
                    } else if (flag.equals("--size")) {
                        String[] sizes = arguments.remove().split(",");
                        sizeX = Integer.parseInt(sizes[0]);
                        sizeY = Integer.parseInt(sizes[1]);
                        file = Optional.empty();
                    } else if (flag.equals("--file")) {
                        sizeX = -1;
                        sizeY = -1;
                        file = Optional.of(new File(arguments.remove()));
                        if (!file.get().isFile()) {
                            throw new IllegalArgumentException("file not found: \"" + file.get() + "\"");
                        }
                    } else {
                        throw new IllegalArgumentException("unknown option: \"" + flag + "\"");
                    }
                } catch (NoSuchElementException nsee) {
                    throw new IllegalArgumentException("missing argument for " + flag);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException("unable to parse number for " + flag);
                }
            }
        } catch (IllegalArgumentException iae) {
            System.err.println(iae.getMessage());
            System.err.println(
                    "usage: MinesweeperServer [--debug | --no-debug] [--port PORT] [--size SIZE_X,SIZE_Y | --file FILE]");
            return;
        }

        try {
            runMinesweeperServer(debug, file, sizeX, sizeY, port);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Start a MinesweeperServer running on the specified port, with either a random new board or a
     * board loaded from a file.
     * 
     * @param debug The server will disconnect a client after a BOOM message if and only if debug is false.
     * @param file If file.isPresent(), start with a board loaded from the specified file,
     *             according to the input file format defined in the documentation for main(..).
     * @param sizeX If (!file.isPresent()), start with a random board with width sizeX
     *              (and require sizeX > 0).
     * @param sizeY If (!file.isPresent()), start with a random board with height sizeY
     *              (and require sizeY > 0).
     * @param port The network port on which the server should listen, requires 0 <= port <= 65535.
     * @throws IOException if a network error occurs
     */
    public static void runMinesweeperServer(boolean debug, Optional<File> file, int sizeX, int sizeY, int port)
            throws IOException {

        // NOTE: at this point, the static variable board is declared (so its assigned to a default value which in Java it is null here)

        // assign the static variable board to an actual board
        if (file.isPresent()) {
            // Load board from file
            MinesweeperServer.board = loadBoardFromFile(file.get());
        } else {
            // Use default 10x10 if size not specified
            MinesweeperServer.board = createRandomBoard(sizeX, sizeY);
        }

        //this is the constructor method for this object
        MinesweeperServer server = new MinesweeperServer(port, debug);
        server.serve();
    }

    //update players
    private synchronized int updatePlayers(int count) {
        players += count;
        return players;
    }
}
