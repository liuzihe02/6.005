/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package minesweeper;

public class Board {

    // Three arrays to track the state of each square

    // true if square has mine
    private final boolean[][] hasMine;
    //Each square is either flagged , dug , or untouched

    // NOTE: 0 is flagged, 1 is dug, 3 is untouched

    private final int[][] status; // true if square has been flagged

    //inclusive range of width, zero indexed
    private final int width;
    private final int height;

    // Create new board
    // takes in where the mines are
    public Board(int width, int height, int[][] mines) {
        this.width = width;
        this.height = height;

        hasMine = new boolean[width][height];
        status = new int[width][height];

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
                //increment the count if there exists a bomb
                if (hasMine[newX][newY]) {
                    count++;
                }
            }
        }
        return count;
    }

    // Dig a square
    //if a bomb goes off - this becomes a dug square, but doesnt trigger the blank-square-recursive digging
    //make sure to update the surrounding squares

    //if theres no bomb, and all adjacent are no bomb, then trigger recursive digging
    // returns whether A BOMB HAS EXPLODED
    public boolean dig(int x, int y) {
        // Can't dig if already dug or flagged, or if out of bounds
        if ((status[x][y] == 0) || (status[x][y] == 1) || (x < 0 || x > width || y < 0 || y > height)) {
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
            // If empty square and no adjacent mines, dig neighbors
            if (countMines(x, y) == 0) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int newX = x + dx;
                        int newY = y + dy;
                        //skip self and dig only the untouched squares
                        if ((dx != 0 && dy != 0) && status[newX][newY] == 2) {
                            dig(newX, newY);
                        }
                    }
                }
            }
            return false;
        }
    }

//     // Flag a square
//     public boolean flag(int x, int y) {
//         if (isRevealed[x][y])
//             return false;
//         isFlagged[x][y] = true;
//         return true;
//     }

//     // Remove flag from a square
//     public boolean deflag(int x, int y) {
//         if (!isFlagged[x][y])
//             return false;
//         isFlagged[x][y] = false;
//         return true;
//     }

//     // Convert board to string for display
//     @Override
//     public String toString() {
//         StringBuilder board = new StringBuilder();

//         for (int y = 0; y < height; y++) {
//             for (int x = 0; x < width; x++) {
//                 if (isFlagged[x][y]) {
//                     board.append("F");
//                 } else if (!isRevealed[x][y]) {
//                     board.append("-");
//                 } else if (hasMine[x][y]) {
//                     board.append("*");
//                 } else {
//                     int mines = countMines(x, y);
//                     board.append(mines == 0 ? " " : mines);
//                 }
//                 if (x < width - 1)
//                     board.append(" ");
//             }
//             if (y < height - 1)
//                 board.append("\n");
//         }

//         return board.toString();
//     }
// }
