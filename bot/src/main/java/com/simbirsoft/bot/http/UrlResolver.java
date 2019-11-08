package com.simbirsoft.bot.http;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

/**
 * This class builds URL later used by {@link ApiRequestPerformer}.
 */
public class UrlResolver {
    private final URL baseUrl;

    public UrlResolver(URL baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URL login(String email, String password) throws Exception {
        return baseUrl.toURI().resolve(
                new URI(String.format("/api/login?email=%s&password=%s", email, password))
        ).toURL();
    }

    public URL unreadMessages(String token) throws Exception {
        return baseUrl.toURI().resolve(
                new URI(String.format("/api/messages/unread?token=%s", token))
        ).toURL();
    }

    public URL send(String receiverEmail, String messageContent, String token) throws Exception {
        return baseUrl.toURI().resolve(
                new URI(String.format(
                        "/api/send?receiver=%s&message=%s&token=%s",
                        receiverEmail,
                        encode(messageContent),
                        token
                ))
        ).toURL();
    }

    private String encode(String parameter) throws UnsupportedEncodingException {
        return URLEncoder.encode(parameter, "UTF-8");
    }
}
