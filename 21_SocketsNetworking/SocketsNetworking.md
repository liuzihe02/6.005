# Sockets and Networking

We examine client/server communication over the network using the socket abstraction
- network communication is inherently concurrent
- need to design the wire protocol that clients and servers use to communicate
- some operations with sockets are *blocking*: they block the progress of a thread until it returns a result

## Client/Server Design Pattern

- A client initiates the communication by connecting to a server
- The client sends requests to the server, and the server sends replies back. 
- Finally, the client disconnects
- A server might handle connections from many clients concurrently, and clients might also connect to multiple servers.

## Network Sockets

### IP Addresses

A network interface is identified by its IP address
- IPv4 addresses are 32-bit numbers written in four 8-bit parts
- `18.9.22.69` is the IP address of a MIT web server
- `127.0.0.1` is the loopback or localhost address: it always refers to the local machine

### Hostnames

Hostnames are names that can be translated into IP addresses.
- A single hostname can map to different IP addresses at different times
- multiple hostnames can map to the same IP address
- `web.mit.edu` is the name for MIT’s web server
  - you can translate this into an IP address:
```bash
$ dig +short web.mit.edu
18.9.22.69
```
This translation is done using the Domain System Name (DNS)

### Port Numbers

A single machine might have multiple server applications that clients wish to connect to, so we need a way to direct traffic on the same network interface to different processes.

Network interfaces have multiple ports identified by a 16-bit number from 0 (which is reserved, so we effectively start at 1) to 65535.

Server processes bind to specific ports to **listen** for incoming connections. Clients must know the port number to connect to a specific service.

Well-known ports for standard service:
- 22: SSH
- 25: Email
- 80: HTTP (Web)

When the port is not a standard port, it is specified as part of the address. For example, the URL `http://128.2.39.10:9000` refers to port `9000` on the machine at `128.2.39.10`

### Network Sockets

A socket represents one end of the connection between client and server.

- A **listening socket** is used by a server process to wait for connections from clients.

  - In Java, use `ServerSocket` to make a listening socket, and use its `accept` method to listen to it.

- A **connected socket** can send and receive messages to and from the process on the other end of the connection.
  - Identified by both the local IP address and port number plus the remote IP address and port number, which allows a server to differentiate between concurrent connections from different IPs, or from the same IP on different remote ports.
  - In Java, clients use a `Socket` constructor to establish a socket connection to a server. Servers obtain a connected socket as a `Socket` object returned from `ServerSocket.accept`

## I/O

### Buffers

The data that clients and servers exchange over the network is sent in chunks. These are rarely just byte-sized chunks, although they might be. The sending side (the client sending a request or the server sending a response) typically writes a large chunk. The network chops that chunk up into packets, and each packet is routed separately over the network. At the other end, the receiver reassembles the packets together into a stream of bytes.

### Streams

The data going into or coming out of a socket is a **stream of bytes**.

In Java, `InputStream` objects represent sources of data flowing into your program. For example:

- Reading from a file on disk with a `File­Input­Stream`
- User input from `System.in`
- Input from a network socket

`OutputStream` objects represent places we can write data to (data sinks)

- `FileOutputStream` for saving to files
- `System.out` is a `PrintStream` , an `OutputStream` that prints readable representations of various types
- Output to a network socket

> With sockets, remember that the output of one process is the input of another process. If Alice and Bob have a socket connection, Alice has an output stream that flows to Bob’s input stream, and vice versa

## Blocking

**Blocking** means that a thread waits (without doing further work) until an event occurs. We can use this term to describe methods and method calls: if a method is a blocking method , then a call to that method can block , waiting until some event occurs before it returns to the caller.

Socket input/output streams exhibit blocking behavior:
- When an incoming socket’s buffer is empty, calling `read` blocks until data are available.
- When the destination socket’s buffer is full, calling `write` blocks until space is available.

Blocking is very convenient from a programmer’s point of view, because the programmer can write code as if the `read` (or `write` ) call will always work, no matter what the timing of data arrival. If data (or for `write` , space) is already available in the buffer, the call might return very quickly. But if the read or write can’t succeed, the call *blocks*. The operating system takes care of the details of delaying that thread until read or write can succeed.

## Using Network Sockets

Refer to `Sockets.md`

## Wire Protocols

A **protocol** is a set of messages that can be exchanged by two communicating parties. A **wire protocol** in particular is a set of messages represented as byte sequences, like hello world and bye (assuming we’ve agreed on a way to encode those characters into bytes).


### HTTP

