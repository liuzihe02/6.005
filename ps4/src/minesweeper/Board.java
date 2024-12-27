/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

import java.util.Arrays;

/**
 * note that we only ever keep track of status and where bombs are
 * so we dynamically calcualte integers and board state; instead of keep a history
 * prevents concurrency and update issues
 * 
 *   
*/

public class Board {

    // Three arrays to track the state of each square

    // true if square has mine
    private final boolean[][] hasMine;
    //Each square is either flagged , dug , or untouched

    // NOTE: 0 is flagged, 1 is dug, 2 is untouched

    private final int[][] status; // true if square has been flagged

    //exclusive range of width, zero indexed
    //actual index goes from 0 to width-1
    private final int width;
    private final int height;

    /**
    * Creates a new Minesweeper board with specified dimensions and mine locations.
    * 
    * @param width Board width (number of columns)
    * @param height Board height (number of rows) 
    * @param mines Array of [x,y] coordinates where mines are located
    * @throws IndexOutOfBoundsException if mine coordinates are invalid
    */
    public Board(int width, int height, int[][] mines) {
        this.width = width;
        this.height = height;

        //NOTE: first index is the outer dimension, last index is the inner dimension
        // so this creates [width] number of 1D array (columns), and each column has [height] number of elements
        hasMine = new boolean[width][height];
        status = new int[width][height];
        //default is to fill all with untouched
        // note that Arrays fill only work for 1D array
        // we use stream for this
        Arrays.stream(status).forEach(row -> Arrays.fill(row, 2));

        // Place mines
        for (int[] mine : mines) {
            int x = mine[0];
            int y = mine[1];
            // no checking here, assumes x and y are valid
            // will be invalid indexing error if not valid anyway
            hasMine[x][y] = true;
        }
    }

    // Count number of mines around a square
    private int countMines(int x, int y) {
        int count = 0;
        //will go from -1,0,1
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                int newX = x + dx;
                int newY = y + dy;
                //skip self, and new coords must also be valid
                //be careful of brackets here
                if ((dx != 0 || dy != 0) && isValid(newX, newY)) {
                    //increment the count if there exists a bomb
                    if (hasMine[newX][newY]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
    * Attempts to dig at specified coordinates.
    * If square is untouched and contains:
    * - A mine: Removes mine and returns true; do not do recursive digging
    * - No mine and no adjacent mines: Recursively digs adjacent untouched squares
    * - No mine but has adjacent mines: Only digs this square
    * 
    * @param x X-coordinate to dig (horizontal)
    * @param y Y-coordinate to dig (vertical)
    * @return true if a bomb was hit, false otherwise
    */
    public boolean dig(int x, int y) {
        // Can't dig if already dug or flagged, or if out of bounds
        // must be untouched
        if (!isValid(x, y)) {
            return false;
        }
        // cant dig if already dug or flagged, must be untouched
        if ((status[x][y] == 0) || (status[x][y] == 1)) {
            return false;
        }

        //make it dug
        status[x][y] = 1;

        //check if bomb

        //bomb
        if (hasMine[x][y]) {
            //change to no bomb
            hasMine[x][y] = false;
            //TODO: if debug flag is missing, then terminate user's connection. take care of this in minesweeperServer
            return true;
        }
        //no bomb
        else {
            //if adjacent all blank, trigger recursive blank digging
            // If no bomb and no adjacent mines, dig neighbors
            if (countMines(x, y) == 0) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int newX = x + dx;
                        int newY = y + dy;
                        //skip self (cannot be both zero) and new box must be valid
                        if ((dx != 0 || dy != 0) && isValid(newX, newY)) {
                            //dig only the untouched squares
                            if (status[newX][newY] == 2) {
                                dig(newX, newY);
                            }
                        }
                    }
                }
            }
            //return false just for this blank
            return false;
        }
    }

    // Flag a square
    // returns true if successfully flagged
    public boolean flag(int x, int y) {
        //if in bound, and is untouched
        if (isValid(x, y) && status[x][y] == 2) {
            //change to flag
            status[x][y] = 0;
            return true;
        }
        //not in bounds or unable to flag
        else {
            return false;
        }
    }

    // deflag a square
    // returns true if successfully deflagged
    public boolean deflag(int x, int y) {
        //if in bound, and is flagged
        if (isValid(x, y) && status[x][y] == 0) {
            //change to untouched
            status[x][y] = 2;
            return true;

        }
        //not in bounds or unable to flag
        else {
            return false;
        }
    }

    /*
     * checks if a given square is in the board (within valid bounds)
     */
    private boolean isValid(int x, int y) {
        return (x >= 0 && x < width && y >= 0 && y < height);
    }

    // Convert board to string for display
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder();

        //traverse vertically
        for (int y = 0; y < height; y++) {
            //build along one row, horizontally
            for (int x = 0; x < width; x++) {
                //flagged
                if (status[x][y] == 0) {
                    board.append("F");
                }
                //untouched
                else if (status[x][y] == 2) {
                    board.append("-");
                }
                // tried to dig before/blank recursive dig
                else {
                    //surrounding number of mines
                    int mines = countMines(x, y);
                    //dug before
                    if (status[x][y] == 1) {
                        //0 neighbour bombs
                        if (mines == 0) {
                            board.append(" ");
                        }
                        // some neighbour bombs
                        else {
                            board.append(Integer.toString(mines));
                        }
                    }
                    //shouldnt go here
                    else {
                        System.out.println(
                                "Debug: Unexpected status " + status[x][y] + " at position [" + x + "][" + y + "]");
                    }
                }
                if (x < width - 1)
                    board.append(" ");
            }
            if (y < height - 1)
                board.append("\n");
        }

        return board.toString();
    }
}
