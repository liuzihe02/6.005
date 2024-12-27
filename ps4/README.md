# Problem Set 4 Minesweeper

Specs are available [here](https://ocw.mit.edu/ans7870/6/6.005/s16/psets/ps4/#tips)

To start the host, we need to compile and run the binary files (make sure you are in root folder `ps4` folder) for this. The `bin` should be parallel to `src` and `test` files.

```bash
# compile files
javac -d bin src/minesweeper/server/*.java src/minesweeper/Board.java
# start the server
java -cp bin minesweeper.server.MinesweeperServer
```

To run the game, again be in the root folder `ps4`. Then run
```bash
telnet localhost 4444
```
