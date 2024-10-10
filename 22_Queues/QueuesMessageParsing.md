# Queues and Message Parsing

## Two Models for Concurrency

- **Shared Memory**: Concurrent modules read and write shared mutable objects. For example, creating multiple threads within a single Java process is shared-memory concurrency
- **Message Passing**: Concurrent modules interact by sending immutable messages to one another over a communication channel. We’ve had one example of message passing so far: the client/server pattern , in which clients and servers are concurrent processes and the communication channel is a network socket

The message passing model has several advantages over the shared memory model, which boil down to greater safety from bugs.
- Concurrent modules interact explicitly , by passing messages through the communication channel, rather than implicitly through mutation of shared data.
- The implicit interaction of shared memory can too easily lead to inadvertent interaction, sharing and manipulating data in parts of the program that don’t know they’re concurrent and aren’t cooperating properly in the thread safety strategy
- Message passing also shares only immutable objects (the messages) between modules, whereas shared memory requires sharing mutable objects, which we have already seen can be a source of bugs

## Message Passing with Threads

Message passing between processes include the client/server communication between network sockets. Message passing between threads in one process is also possible; this design preferred over shared memory model with locks.

Use a synchronized queue for message passing between threads.

Basic `Queue` Interface:
-  `add(e)` adds element `e` to the end of the queue.
- `remove()` removes and returns the element at the head of the queue, or throws an exception if the queue is empty

`BlockingQueue` interface:
- `put(e)` *blocks* until it can add element `e` to the end of the queue (if the queue does not have a size bound, `put` will not block)
- `take()` *blocks* until it can remove and return the element at the head of the queue, waiting until the queue is non-empty

Producer-consumer design pattern for message passing between threads
- Producer threads and consumer threads share a synchronized queue
- Producers put data or requests onto the queue, and consumers remove and process them
- One or more producers and one or more consumers might all be adding and removing items from the same queue, so this queue must be safe for concurrency!

Java provides two implementations of `BlockingQueue` :

- `ArrayBlockingQueue` is a fixed-size queue that uses an array representation. `put` a new item on the queue will block if the queue is full.
- `LinkedBlockingQueue` is a growable queue using a linked-list representation. If no maximum capacity is specified, the queue will never fill up, so put will never block.

Instead of designing a wire protocol, we must choose or design a type for messages in the queue. It **must be an immutable type**. And just as we did with operations on a threadsafe ADT or messages in a wire protocol, we must design our messages here to prevent race conditions and enable clients to perform the atomic operations they need.

## Implementing Message Passing with Queues

The below `Squarer` class takes in requests to square integers and sends out the text replies of the results:

```java
/** Squares integers. */
public class Squarer {

    private final BlockingQueue<Integer> in;
    private final BlockingQueue<SquareResult> out;
    // Rep invariant: in, out != null

    /** Make a new squarer.
     *  @param requests queue to receive requests from
     *  @param replies queue to send replies to */
    public Squarer(BlockingQueue<Integer> requests,
                   BlockingQueue<SquareResult> replies) {
        this.in = requests;
        this.out = replies;
    }

    /** Start handling squaring requests. */
    public void start() {
        new Thread(new Runnable() {
            public void run() {
                //keep looping to take requests and put them in results
                while (true) {
                    // TODO: we may want a way to stop the thread
                    try {
                        // block until a request arrives
                        int x = in.take();
                        // compute the answer and send it back
                        int y = x * x;
                        out.put(new SquareResult(x, y));
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
```

Outgoing messages are instances of `SquareResult`:
```java
/** An immutable squaring result message. */
public class SquareResult {
    private final int input;
    private final int output;

    /** Make a new result message.
     *  @param input input number
     *  @param output square of input */
    public SquareResult(int input, int output) {
        this.input = input;
        this.output = output;
    }

    @Override public String toString() {
        return input + "^2 = " + output;
    }
}
```

Main method that uses `squarer`:
```java
public static void main(String[] args) {

    BlockingQueue<Integer> requests = new LinkedBlockingQueue<>();
    BlockingQueue<SquareResult> replies = new LinkedBlockingQueue<>();

    Squarer squarer = new Squarer(requests, replies);
    squarer.start();

    try {
        // make a request
        requests.put(42);
        // ... maybe do something concurrently ...
        // read the reply
        System.out.println(replies.take());
    } catch (InterruptedException ie) {
        ie.printStackTrace();
    }
}
```

## Stopping

If we want to stop the `Squarer` so it is no longer waiting for new inputs, we need to shut down this specific thread.

### Poison Pill

This is a special message on the queue that signals the consumer to end. We could choose a magic poison integer like `0` for `Squarer`, since no one would square `0`:

```java
public void run() {
    while (true) {
        try {
            // block until a request arrives
            SquareRequest req = in.take();
            // see if we should stop
            if (req.shouldStop()) { break; }
            // compute the answer and send it back
            int x = req.input();
            int y = x * x;
            out.put(new SquareResult(x, y));
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
```

### Interrupt

It is also possible to interrupt a thread by calling its `interrupt()` method. If the thread is blocked waiting, the method it’s blocked in will throw an `InterruptedException` (that’s why we have to `try-catch` that exception almost any time we call a blocking method).

```java
public void run() {
    // handle requests until we are interrupted
    // this handles interruptions between iterations
    while ( ! Thread.interrupted()) {
        //this try except block handles interruptions in the take method
        try {
            // block until a request arrives
            int x = in.take();
            // compute the answer and send it back
            int y = x * x;
            out.put(new SquareResult(x, y));
        } catch (InterruptedException ie) {
            // stop
            break;
        }
    }
}
```