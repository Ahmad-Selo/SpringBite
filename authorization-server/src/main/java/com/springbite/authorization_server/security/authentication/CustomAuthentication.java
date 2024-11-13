package com.springbite.authorization_server.security.authentication;

import com.springbite.authorization_server.model.SecurityUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
public class CustomAuthentication extends JwtAuthenticationToken {

    private final SecurityUser securityUser;

    public CustomAuthentication(
            Jwt jwt,
            Collection<? extends GrantedAuthority> authorities,
            SecurityUser securityUser
    ) {
        super(jwt, authorities);

        this.securityUser = securityUser;
    }


}
