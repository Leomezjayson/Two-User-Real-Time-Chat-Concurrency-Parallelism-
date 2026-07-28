# UDP Echo Client/Server (Remote Procedure Calls)

A simple client/server pair built with raw Java sockets (`java.net.DatagramSocket`)
demonstrating UDP communication — no external networking libraries used.

## What it does

- The client sends a text message to the server.
- The server:
  - Prints the client's IP address and port to the console.
  - Inverts the case of every letter in the message (`Hello` → `hELLO`).
  - Sends the inverted message back.
- The client prints the reply.

This project corresponds to the course topic **Remote Procedure Calls (RPC)** —
demonstrating the fundamental request/response pattern that RPC frameworks
build on top of, implemented here at the raw socket level.

## Why UDP

UDP (`DatagramSocket`/`DatagramPacket`) is connectionless — there's no handshake
before sending data, unlike TCP. This makes it simple and fast, at the cost of
no delivery guarantee. It's well suited to a simple request/reply exchange like
this one.

## How to run

**1. Compile both files:**
```bash
javac EchoServer.java EchoClient.java
```

**2. Start the server** (in one terminal):
```bash
java EchoServer
```

**3. Start the client** (in a separate terminal):
```bash
java EchoClient
```

**4. Type messages in the client.** Type `exit` to quit.

### Example

```
You: Hello
Server: hELLO

You: Goodbye World
Server: gOODBYE wORLD
```

Server console output:
```
Received from 127.0.0.1:39722 -> "Hello"
Replied with -> "hELLO"
Received from 127.0.0.1:39722 -> "Goodbye World"
Replied with -> "gOODBYE wORLD"
```

## Key concepts demonstrated

- **Raw socket programming**: `DatagramSocket` and `DatagramPacket`, no
  networking libraries or frameworks.
- **Connectionless communication**: each packet is independent; the server
  doesn't maintain a persistent connection to the client.
- **Byte-level data transfer**: strings are converted to/from raw bytes
  (`getBytes()` / `new String(...)`) — the network only understands bytes,
  not Java objects, so this conversion is required for any data to travel
  over a socket.
- **Client identification**: UDP packets carry the sender's address and port
  automatically, which the server reads via `getAddress()` and `getPort()`.

## Tech stack

- Java 21 (no external dependencies)

