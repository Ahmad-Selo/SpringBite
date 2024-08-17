package com.springbite.authorization_server.models.dtos;

import com.springbite.authorization_server.security.JwkSet;

import java.util.List;

public class JwkSetResponse {

    private List<JwkSet> keys;

    public JwkSetResponse() {
    }

    public JwkSetResponse(List<JwkSet> keys) {
        this.keys = keys;
    }

    public List<JwkSet> getKeys() {
        return keys;
    }

    public void setKeys(List<JwkSet> keys) {
        this.keys = keys;
    }
}
