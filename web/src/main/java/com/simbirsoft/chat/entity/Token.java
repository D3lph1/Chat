package com.simbirsoft.chat.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Date;

@Embeddable
public class Token {
    @Column(name = "token", unique = true)
    private String value;

    @Column(name = "expired_at")
    private Date expiredAt;

    public Token(String value, Date expiredAt) {
        this.value = value;
        this.expiredAt = expiredAt;
    }

    public Token() {
    }

    public String getValue() {
        return value;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }
}
