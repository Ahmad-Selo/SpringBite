package com.springbite.client_server.entities;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "client_registrations")
public class ClientRegistrationEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String registrationId;

    private String clientId;

    private String clientSecret;

    private String clientAuthenticationMethod;

    private String authorizationGrantType;

    private String redirectUri;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "scopes")
    private Set<String> scopes;

    private String clientName;

    public ClientRegistrationEntity() {
    }

    public ClientRegistrationEntity(String registrationId,
                                    String clientId,
                                    String clientSecret,
                                    String clientAuthenticationMethod,
                                    String authorizationGrantType,
                                    String redirectUri,
                                    Set<String> scopes,
                                    String clientName) {
        this.registrationId = registrationId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientAuthenticationMethod = clientAuthenticationMethod;
        this.authorizationGrantType = authorizationGrantType;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.clientName = clientName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientAuthenticationMethod() {
        return clientAuthenticationMethod;
    }

    public void setClientAuthenticationMethod(String clientAuthenticationMethod) {
        this.clientAuthenticationMethod = clientAuthenticationMethod;
    }

    public String getAuthorizationGrantType() {
        return authorizationGrantType;
    }

    public void setAuthorizationGrantType(String authorizationGrantType) {
        this.authorizationGrantType = authorizationGrantType;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRegistrationEntity that = (ClientRegistrationEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(registrationId, that.registrationId) &&
                Objects.equals(clientId, that.clientId) &&
                Objects.equals(clientSecret, that.clientSecret) &&
                Objects.equals(clientAuthenticationMethod, that.clientAuthenticationMethod) &&
                Objects.equals(authorizationGrantType, that.authorizationGrantType) &&
                Objects.equals(redirectUri, that.redirectUri) &&
                Objects.equals(scopes, that.scopes) &&
                Objects.equals(clientName, that.clientName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                registrationId,
                clientId,
                clientSecret,
                clientAuthenticationMethod,
                authorizationGrantType,
                redirectUri,
                scopes,
                clientName
        );
    }
}
