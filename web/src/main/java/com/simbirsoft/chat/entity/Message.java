package com.simbirsoft.chat.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "read", nullable = false)
    private boolean read = false;

    @ManyToOne(targetEntity = User.class)
    private User sender;

    @ManyToOne(targetEntity = User.class)
    private User receiver;

    public Message(String content, User sender, User receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    public Message() {
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public boolean isRead() {
        return read;
    }

    public void read() {
        read = true;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        String content = getContent();
        if (content.length() >= 20) {
            content = content.substring(0, 20) + "...";
        }

        return String.format(
                "%s(id=%d, content=\"%s\", sender=%s, receiver=%s)",
                getClass().getName(),
                getId(),
                content,
                getSender(),
                getReceiver()
        );
    }
}
