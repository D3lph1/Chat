package com.simbirsoft.chat.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", length = 64, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 64, nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    /**
     * If password is null then user authenticated by OAuth.
     */
    @Column(name = "password")
    private String password;

    @Column(name = "bot", nullable = false)
    private boolean bot = false;

    @Embedded
    private Token token;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    @PrePersist
    public void onCreate() {
        createdAt = new Date();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        return ((User) obj).getId() == getId();
    }

    @Override
    public String toString() {
        return String.format(
                "%s(id=%d, firstName=%s, lastName=%s, email=%s)",
                getClass().getName(),
                getId(),
                getFirstName(),
                getLastName(),
                getEmail()
        );
    }
}
