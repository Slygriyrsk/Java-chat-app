package com.chatapp.shared;

import java.io.Serializable;

public class User implements Serializable {
    // Yeh class User ka hai, jo Serializable interface ko implement karta hai.
    // Isme username aur online status store hota hai.

    private String username; // User ka naam
    private boolean online;  // Kya user online hai ya nahi

    public User(String username, boolean online) {
        // Constructor jo username aur online status set karta hai
        this.username = username; 
        this.online = online; 
    }

    // Getters aur setters
    public String getUsername() { return username; } // Username lene ke liye
    public void setUsername(String username) { this.username = username; } // Username set karne ke liye
    public boolean isOnline() { return online; } // Online status lene ke liye
    public void setOnline(boolean online) { this.online = online; } // Online status set karne ke liye
}