package com.chatapp;

import com.chatapp.client.ClientGUI;
import com.chatapp.server.ChatServer;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        /* 
        Yahan check kar rahe hain ki application server ya client ke roop mein chalegi.
        Agar args mein pehla argument "server" hai, toh server start hoga.
        Agar nahi, toh client GUI launch hoga.
        */
        if (args.length > 0 && args[0].equalsIgnoreCase("server")) {
            // Start the server
            new ChatServer().start();
        } else {
            // Start the client GUI
            Application.launch(ClientGUI.class, args);
        }
    }
}