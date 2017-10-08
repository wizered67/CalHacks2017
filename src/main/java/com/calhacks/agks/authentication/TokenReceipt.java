package com.calhacks.agks.authentication;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenReceipt {
    private String token;
    private String id;

    public TokenReceipt() {}

    public TokenReceipt(String token, String userId) {
        this.token = token;
        this.id = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
