package com.springbite.authorization_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.springbite.authorization_server.entities.KeyPairEntity;
import com.springbite.authorization_server.filters.ClientAuthFilter;
import com.springbite.authorization_server.filters.JwtAuthFilter;
import com.springbite.authorization_server.repositories.JwkRepository;
import com.springbite.authorization_server.repositories.KeyPairRepository;
import com.springbite.authorization_server.services.JwkService;
import com.springbite.authorization_server.services.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.springbite.authorization_server.utils.KeyPairUtil.*;

@Configuration
@ConfigurationProperties(prefix = "trusted")
public class SecurityConfig {

    private final KeyPairRepository keyPairRepository;
    private final JwkRepository jwkRepository;

    private Set<String> issuers;
    private Set<String> clients;

    public SecurityConfig(KeyPairRepository keyPairRepository, JwkRepository jwkRepository) {
        this.keyPairRepository = keyPairRepository;
        this.jwkRepository = jwkRepository;
    }

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

    @Bean
    public JwtService jwtService() {
        return new JwtService(issuers, clients);
    }

    @Bean
    public JwkService jwkService() {
        return new JwkService(jwkRepository);
    }

    @Bean
    @Order(1)
    public SecurityFilterChain asFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration
                .applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.exceptionHandling(
                e -> e.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login")
                )
        );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            RegisteredClientRepository registeredClientRepository)
            throws Exception {
        http.formLogin(Customizer.withDefaults());

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/login", "/signup/**", "/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(
                new JwtAuthFilter(jwtService(), jwkService()),
                UsernamePasswordAuthenticationFilter.class
        );

        http.addFilterBefore(
                new ClientAuthFilter(registeredClientRepository, passwordEncoder()),
                JwtAuthFilter.class
        );

        http.csrf(
                c -> c.ignoringRequestMatchers("/login", "/signup/**", "/auth/**")
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Transactional
    public RSAKey rsaKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        long id = 1;

        Optional<KeyPairEntity> keyPairEntityOptional = keyPairRepository.findById(id);

        RSAKey rsaKey;

        if (keyPairEntityOptional.isPresent()) {
            KeyPairEntity keyPairEntity = keyPairEntityOptional.get();
            RSAPublicKey publicKey = decodePublicKey(keyPairEntity.getPublicKey());
            RSAPrivateKey privateKey = decodePrivateKey(keyPairEntity.getPrivateKey());

            rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(String.valueOf(id))
                    .build();
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(String.valueOf(id))
                    .build();

            KeyPairEntity keyPairEntity = new KeyPairEntity();
            keyPairEntity.setId(id);
            keyPairEntity.setPublicKey(encodePublicKey(publicKey));
            keyPairEntity.setPrivateKey(encodePrivateKey(privateKey));

            keyPairRepository.save(keyPairEntity);
        }
        return rsaKey;
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAKey rsaKey = rsaKey();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            Authentication authentication = context.getPrincipal();
            JwtClaimsSet.Builder claims = context.getClaims();
            claims.claim("authorities",
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        };
    }
}
