# Two-User-Real-Time-Chat-Concurrency-Parallelism-
This describes a chat application built with Java. It allows two users to connect to a central server and send messages back and forth in real-time. The communication happens directly over the internet using basic Java networking capabilities (TCP sockets), without relying on any pre-built chat libraries or frameworks.

What it does
The server waits for exactly two clients to connect.
Once both are connected, any message sent by one client is immediately relayed to the other.
Each client can send and receive messages at the same time — you don't have to wait for a reply before typing your next message.
This corresponds to the course topic Concurrency and Parallelism.

Why concurrency is required here (not optional)
Without threads, a program can only do one blocking operation at a time.

On the server: if it read from Client A first, it would be stuck waiting for Client A to send something before it could even check Client B — freezing the whole conversation in one direction at a time. 
The fix: each client gets its own thread (ClientHandler), so the server can wait on both clients simultaneously.
On the client: a single thread can either wait for user input OR wait for an incoming message — not both at once. The fix: a listener thread runs in the background continuously checking for incoming messages, while the main thread stays free to handle what the user types.
This is the core idea behind concurrency: multiple tasks making progress independently, without one blocking the other.

How to run
1. Compile:

bash
javac ChatServer.java ChatClient.java
2. Start the server (one terminal):

bash
java ChatServer
3. Start Client A (a second terminal):

bash
java ChatClient
4. Start Client B (a third terminal):

bash
java ChatClient
5. Type messages in either client window — they'll appear instantly in the other. Type exit in either client to disconnect it.

Example
Client A:

You: Hello from A
Peer: Hi A, this is B
Client B:

Peer: Hello from A
You: Hi A, this is B
Server console:

Client A connected: /127.0.0.1:53462
Client B connected: /127.0.0.1:53476
Both clients connected — starting relay.

Client A -> Hello from A
Client B -> Hi A, this is B

Key concepts demonstrated
Thread-per-client model: the server spawns one thread per connected client, allowing simultaneous, independent handling of both.
Concurrent send/receive on the client: a dedicated listener thread plus the main thread together allow full-duplex (two-way, simultaneous) communication over a single socket.
Blocking I/O with threads: readLine() blocks the calling thread until data arrives — threads are what let multiple blocking operations happen at once without freezing the whole program.

TCP sockets: unlike the UDP-based Echo project, this uses TCP (ServerSocket/Socket), which is connection-based and guarantees ordered, reliable delivery — appropriate for a chat application where message order matters.

Tech stack
Java 21+ (no external dependencies)

