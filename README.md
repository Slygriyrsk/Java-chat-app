## Overview
A Java-based multi-user chat application featuring real-time messaging, private chats, and a graphical user interface. This project highlights networking, multi-threading, and GUI development using JavaFX.

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 11 or later
- Maven

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/Slygriyrsk/Java-chat-app.git
   cd Java-chat-app
   ```

1.  Build the project:

    ```bash
    mvn clean install
    ```

### Running the Application

1.  Start the server:

    ```bash
    mvn javafx:run -Djavafx.args=server
    ```

2.  Start the client(s):

    ```bash
    mvn javafx:run
    ```

Key Concepts
------------

### Networking

Uses Java's `java.net` package for communication. A `ServerSocket` listens for client connections, while `Socket` facilitates data exchange between server and clients.

### Threading

Multi-threading ensures simultaneous client handling and a responsive GUI. The server spawns a thread per client, and the client uses a thread for message reception.

### Socket Programming

The server and clients use sockets to send and receive data streams. This enables efficient, real-time communication.

### GUI Development

The GUI is designed with JavaFX, offering a modern, responsive interface for seamless user interaction.

### Serialization

Objects like `Message` and `User` are serialized for easy transmission between clients and server over the network.

Project Structure
-----------------

```
ChatApplication/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/chatapp/
│   │   │   │   ├── client/
│   │   │   │   │   ├── ChatClient.java
│   │   │   │   │   ├── ClientGUI.java
│   │   │   │   ├── server/
│   │   │   │   │   ├── ChatServer.java
│   │   │   │   │   ├── ClientHandler.java
│   │   │   │   ├── shared/
│   │   │   │   │   ├── Message.java
│   │   │   │   │   ├── User.java
│   │   │   │   ├── Main.java
│   │   ├── resources/
│   │   │   ├── styles.css
├── pom.xml
├── README.md
```

### Structure Rationale

-   **`client/`**: Contains client-side logic, including GUI and network client.
-   **`server/`**: Includes server-side classes like `ChatServer` and `ClientHandler`.
-   **`shared/`**: Houses shared models (`Message`, `User`) used by both client and server.
-   **`Main.java`**: Entry point for running server or client.
-   **`resources/`**: Contains assets like CSS for GUI styling.

Usage Instructions
------------------

1.  Start the server application.
2.  Launch one or more client applications.
3.  Enter a username in the client GUI to connect.
4.  Begin chatting! Use the GUI for public or private messaging.

### Developer Notes

-   Extend `ChatServer` and `ClientHandler` for features like authentication or message persistence.
-   Enhance `ClientGUI` for improved user experience or additional functionality.
-   Modify `Message` and `User` models for extended data exchange.

Enjoy building and exploring this Java chat application!