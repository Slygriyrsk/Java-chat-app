package com.chatapp.shared;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    /* 
    Yeh class Message ka hai, jo ek message ko represent karta hai.
    Isme sender, content, timestamp, aur recipient ki information hoti hai.
    */
    private String sender;
    private String content;
    private LocalDateTime timestamp;
    private String recipient;

     public Message(String sender, String content, String recipient) {
        /* 
        Constructor hai jo message create karta hai.
        Sender, content, aur recipient ko set karta hai.
        Timestamp ko current time se set karta hai.
        */
        this.sender = sender;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.recipient = recipient;
    }

    // Getters and setters
    /* 
    Yeh methods hai jo sender, content, timestamp, aur recipient ki values ko 
    get aur set karne ke liye use hote hain.
    */
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }
}