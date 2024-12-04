package com.chatapp.server;

import com.chatapp.shared.Message;
import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatServer server;
    private String username;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    // private BufferedReader reader;

    public ClientHandler(Socket socket, ChatServer server) {
        this.clientSocket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            // Yahan input aur output streams ko initialize kar rahe hain
            input = new ObjectInputStream(clientSocket.getInputStream());
            output = new ObjectOutputStream(clientSocket.getOutputStream());

            // reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // User ko authenticate karne ka function call kar rahe hain
            authenticateUser();

            while (true) {
                // Message ko read kar rahe hain
                Message message = (Message) input.readObject();
                // Agar recipient null hai, toh broadcast karte hain
                if (message.getRecipient() == null) {
                    server.broadcastMessage(message.getSender() + ": " + message.getContent(), message.getSender());
                } else {
                    // Agar recipient hai, toh private message bhejte hain
                    server.sendPrivateMessage(message.getSender(), message.getRecipient(), message.getContent());
                }
            }

            // String input;
            // while ((input = reader.readLine()) != null) {
            //     Message message = Message.fromJson(input);
            //     if (message.getContent().startsWith("FILE_TRANSFER:")) {
            //         // Handle file transfer
            //         String fileName = message.getContent().substring("FILE_TRANSFER:".length());
            //         receiveFile(fileName);
            //     } else {
            //         // Handle regular message
            //         server.broadcastMessage(message);
            //     }
            // }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Client ko remove karte hain aur socket close karte hain
            server.removeClient(username);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // private void receiveFile(String fileName) throws IOException {
    //     File receivedFile = new File("received_files/" + fileName);
    //     receivedFile.getParentFile().mkdirs();
    //     try (FileOutputStream fos = new FileOutputStream(receivedFile)) {
    //         byte[] buffer = new byte[4096];
    //         int bytesRead;
    //         while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
    //             fos.write(buffer, 0, bytesRead);
    //             if (bytesRead < 4096) break; // Assume end of file
    //         }
    //     }
    //     server.broadcastMessage(new Message("Server", username + " sent a file: " + fileName, null));
    // }

    // User ko authenticate karne ka function
    private void authenticateUser() throws IOException, ClassNotFoundException {
        Message authMessage = (Message) input.readObject();
        this.username = authMessage.getSender();
        server.addClient(username, this);
    }

    // Server se message bhejne ka function
    public void sendMessage(String message) {
        try {
            output.writeObject(new Message("Server", message, null));
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}