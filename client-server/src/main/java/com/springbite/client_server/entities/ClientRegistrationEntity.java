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

    private String authorizationUri;

    private String tokenUri;

    private String jwkSetUri;

    private String issuerUri;

    private String userInfoUri;

    private String userNameAttributeName;

    public ClientRegistrationEntity() {
    }


    public ClientRegistrationEntity(
            String registrationId,
            String clientId,
            String clientSecret,
            String clientAuthenticationMethod,
            String authorizationGrantType,
            String redirectUri,
            Set<String> scopes,
            String clientName,
            String authorizationUri,
            String tokenUri,
            String jwkSetUri,
            String issuerUri,
            String userInfoUri,
            String userNameAttributeName) {
        this.registrationId = registrationId;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.clientAuthenticationMethod = clientAuthenticationMethod;
        this.authorizationGrantType = authorizationGrantType;
        this.redirectUri = redirectUri;
        this.scopes = scopes;
        this.clientName = clientName;
        this.authorizationUri = authorizationUri;
        this.tokenUri = tokenUri;
        this.jwkSetUri = jwkSetUri;
        this.issuerUri = issuerUri;
        this.userInfoUri = userInfoUri;
        this.userNameAttributeName = userNameAttributeName;
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

    public String getAuthorizationUri() {
        return authorizationUri;
    }

    public void setAuthorizationUri(String authorizationUri) {
        this.authorizationUri = authorizationUri;
    }

    public String getTokenUri() {
        return tokenUri;
    }

    public void setTokenUri(String tokenUri) {
        this.tokenUri = tokenUri;
    }

    public String getJwkSetUri() {
        return jwkSetUri;
    }

    public void setJwkSetUri(String jwkSetUri) {
        this.jwkSetUri = jwkSetUri;
    }

    public String getIssuerUri() {
        return issuerUri;
    }

    public void setIssuerUri(String issuerUri) {
        this.issuerUri = issuerUri;
    }

    public String getUserInfoUri() {
        return userInfoUri;
    }

    public void setUserInfoUri(String userInfoUri) {
        this.userInfoUri = userInfoUri;
    }

    public String getUserNameAttributeName() {
        return userNameAttributeName;
    }

    public void setUserNameAttributeName(String userNameAttributeName) {
        this.userNameAttributeName = userNameAttributeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRegistrationEntity entity = (ClientRegistrationEntity) o;
        return Objects.equals(id, entity.id) &&
                Objects.equals(registrationId, entity.registrationId) &&
                Objects.equals(clientId, entity.clientId) &&
                Objects.equals(clientSecret, entity.clientSecret) &&
                Objects.equals(clientAuthenticationMethod, entity.clientAuthenticationMethod) &&
                Objects.equals(authorizationGrantType, entity.authorizationGrantType) &&
                Objects.equals(redirectUri, entity.redirectUri) &&
                Objects.equals(scopes, entity.scopes) &&
                Objects.equals(clientName, entity.clientName) &&
                Objects.equals(authorizationUri, entity.authorizationUri) &&
                Objects.equals(tokenUri, entity.tokenUri) &&
                Objects.equals(jwkSetUri, entity.jwkSetUri) &&
                Objects.equals(issuerUri, entity.issuerUri) &&
                Objects.equals(userInfoUri, entity.userInfoUri) &&
                Objects.equals(userNameAttributeName, entity.userNameAttributeName);
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
                clientName,
                authorizationUri,
                tokenUri,
                jwkSetUri,
                issuerUri,
                userInfoUri,
                userNameAttributeName);
    }
}
