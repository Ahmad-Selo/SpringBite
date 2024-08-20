package com.springbite.authorization_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "csrf")
public class CsrfWhiteSet {

    Set<String> whiteSetUris;

    public Set<String> getWhiteSetUris() {
        return whiteSetUris;
    }

    public void setWhiteSetUris(Set<String> whiteSetUris) {
        this.whiteSetUris = whiteSetUris;
    }
}
