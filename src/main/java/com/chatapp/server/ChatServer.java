package com.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private static final int PORT = 5000;
    private ConcurrentHashMap<String, ClientHandler> clients = new ConcurrentHashMap<>();

    public void start() {
        // ye method server ko start karta hai aur clients ko accept karta hai
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started on port " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);
                
                // ClientHandler ka object banao aur naye thread mein chalao
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, String sender) {
        // Ye method sabhi clients ko message bhejne ke liye hai
        clients.forEach((username, handler) -> {
            if (!username.equals(sender)) {
                handler.sendMessage(message);
            }
        });
    }

    public void addClient(String username, ClientHandler handler) {
        // Ye method naye client ko add karta hai aur sabko notify karta hai
        clients.put(username, handler);
        broadcastMessage(username + " has joined the chat.", "Server");
    }

    public void removeClient(String username) {
        // Ye method client ko remove karta hai aur sabko notify karta hai
        clients.remove(username);
        broadcastMessage(username + " has left the chat.", "Server");
    }

    public void sendPrivateMessage(String sender, String recipient, String message) {
        // Ye method private message bhejne ke liye hai
        ClientHandler recipientHandler = clients.get(recipient);
        if (recipientHandler != null) {
            recipientHandler.sendMessage("[Private from " + sender + "]: " + message);
        }
    }
}