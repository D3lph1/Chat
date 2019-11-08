package com.simbirsoft.chat.service.auth.oauth2;

public abstract class OAuth2UserInfo {
    protected String id;

    protected String name;

    protected String email;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
