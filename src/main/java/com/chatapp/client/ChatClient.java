package com.chatapp.client;

import com.chatapp.shared.Message;
import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class ChatClient {
    private String serverAddress;
    private int serverPort;
    private String username;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Consumer<Message> messageHandler;
    private Consumer<Boolean> statusHandler;

    public ChatClient(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    // public void sendFile(File file) throws IOException {
    //     try (FileInputStream fis = new FileInputStream(file)) {
    //         byte[] buffer = new byte[4096];
    //         int bytesRead;
            
    //         // Send a special message to indicate file transfer
    //         sendMessage(new Message(username, "FILE_TRANSFER:" + file.getName(), null));
            
    //         // Send file contents
    //         while ((bytesRead = fis.read(buffer)) != -1) {
    //             outputStream.write(buffer, 0, bytesRead);
    //         }
    //         outputStream.flush();
    //     }
    // }

    public void start() throws IOException {
        // Socket connection establish kar rahe hain server se
        Socket socket = new Socket(serverAddress, serverPort);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());

        // Messages receive karne ke liye ek naya thread shuru kar rahe hain
        new Thread(this::receiveMessages).start();
    }

    private void receiveMessages() {
        try {
            while (true) {
                // Message ko read kar rahe hain
                Message message = (Message) input.readObject();
                if (messageHandler != null) {
                    // Agar messageHandler set hai, toh message ko handle kar rahe hain
                    messageHandler.accept(message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Agar error aata hai, toh statusHandler ko false pass kar rahe hain
            if (statusHandler != null) {
                statusHandler.accept(false);
            }
        }
    }

    public void sendMessage(Message message) throws IOException {
        // Message ko server ko bhej rahe hain
        output.writeObject(message);
        output.flush();
    }

    public void setUsername(String username) throws IOException {
        this.username = username;
        // User join hone ka message bhej rahe hain
        sendMessage(new Message(username, "joined the chat", null));
        if (statusHandler != null) {
            // StatusHandler ko true pass kar rahe hain
            statusHandler.accept(true);
        }
    }

    public void setMessageHandler(Consumer<Message> handler) {
        // Message handler set kar rahe hain
        this.messageHandler = handler;
    }

    public void setStatusHandler(Consumer<Boolean> handler) {
        // Status handler set kar rahe hain
        this.statusHandler = handler;
    }

    public String getUsername() {
        // Username return kar rahe hain
        return username;
    }
}