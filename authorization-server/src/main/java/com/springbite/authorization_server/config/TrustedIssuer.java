package com.springbite.authorization_server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties("trusted")
public class TrustedIssuer {

    private Set<String> issuers;

    private Set<String> clients;

    public Set<String> getIssuers() {
        return issuers;
    }

    public void setIssuers(Set<String> issuers) {
        this.issuers = issuers;
    }

    public Set<String> getClients() {
        return clients;
    }

    public void setClients(Set<String> clients) {
        this.clients = clients;
    }
}
