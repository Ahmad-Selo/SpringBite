package com.springbite.authorization_server.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.springbite.authorization_server.entity.KeyPairEntity;
import com.springbite.authorization_server.entity.User;
import com.springbite.authorization_server.exception.UserNotFoundException;
import com.springbite.authorization_server.filter.PasswordResetTokenFilter;
import com.springbite.authorization_server.filter.TokenAuthenticationFilter;
import com.springbite.authorization_server.handler.CustomAccessDeniedHandler;
import com.springbite.authorization_server.handler.CustomAuthenticationFailureHandler;
import com.springbite.authorization_server.handler.CustomAuthenticationSuccessHandler;
import com.springbite.authorization_server.handler.CustomLogoutSuccessHandler;
import com.springbite.authorization_server.mixin.LongMixin;
import com.springbite.authorization_server.mixin.SecurityUserMixin;
import com.springbite.authorization_server.model.GoogleProvider;
import com.springbite.authorization_server.model.Provider;
import com.springbite.authorization_server.model.SecurityUser;
import com.springbite.authorization_server.repository.KeyPairRepository;
import com.springbite.authorization_server.repository.UserRepository;
import com.springbite.authorization_server.security.authentication.converter.GoogleAuthenticationConverter;
import com.springbite.authorization_server.security.authentication.converter.JwtAuthenticationConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.CoreJackson2Module;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.springbite.authorization_server.util.KeyPairUtil.*;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final Log logger = LogFactory.getLog(SecurityConfig.class);

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final KeyPairRepository keyPairRepository;

    @Value("${jwk.kid}")
    private String kid;

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration
                .applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());

        http.oauth2ResourceServer(
                o -> o.jwt(
                        Customizer.withDefaults()
                )
        );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver,
            PasswordResetTokenFilter passwordResetTokenFilter,
            TokenAuthenticationFilter tokenAuthenticationFilter
    ) throws Exception {
        http.addFilterAfter(
                tokenAuthenticationFilter,
                BearerTokenAuthenticationFilter.class
        );

        http.addFilterBefore(
                passwordResetTokenFilter,
                BearerTokenAuthenticationFilter.class
        );

        http.formLogin(
                c -> c.successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
        );

        http.logout(
                l -> l.logoutSuccessHandler(logoutSuccessHandler)
        );

        http.oauth2ResourceServer(
                o -> o.authenticationManagerResolver(authenticationManagerResolver)
                        .accessDeniedHandler(accessDeniedHandler)
        );

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/login", "/signup", "/email/**",
                                "/forgot-password", "/verify-code", "/reset-password").permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf(c -> c.disable());

        return http.build();
    }

    @Bean
    public AuthenticationManagerResolver<HttpServletRequest> authenticationManagerResolver(
            RequestMatcher requestMatcher,
            JwtDecoder decoder,
            @Qualifier("googleDecoder") JwtDecoder googleDecoder,
            JwtAuthenticationConverter jwtAuthenticationConverter,
            GoogleAuthenticationConverter googleAuthenticationConverter
    ) {
        JwtAuthenticationProvider jwtAuthProvider = new JwtAuthenticationProvider(decoder);
        jwtAuthProvider.setJwtAuthenticationConverter(jwtAuthenticationConverter);
        AuthenticationManager jwtAuth = new ProviderManager(jwtAuthProvider);

        JwtAuthenticationProvider googleAuthProvider = new JwtAuthenticationProvider(googleDecoder);
        googleAuthProvider.setJwtAuthenticationConverter(googleAuthenticationConverter);
        AuthenticationManager googleAuth = new ProviderManager(googleAuthProvider);

        return request -> {
            if(!requestMatcher.matches(request)) {
                return jwtAuth;
            }

            String uri = request.getRequestURI();

            if(uri.equals(GoogleProvider.GOOGLE_URI)) {
                return googleAuth;
            } else {
                return jwtAuth;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PrePostTemplateDefaults prePostTemplateDefaults() {
        return new PrePostTemplateDefaults();
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    @Bean
    public OAuth2AuthorizationService auth2AuthorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository
    ) {
        JdbcOAuth2AuthorizationService oAuth2AuthorizationService = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);

        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper authorizationRowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);

        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new CoreJackson2Module());

        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);

        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        objectMapper.addMixIn(Long.class, LongMixin.class);
        objectMapper.addMixIn(SecurityUser.class, SecurityUserMixin.class);

        authorizationRowMapper.setObjectMapper(objectMapper);

        oAuth2AuthorizationService.setAuthorizationRowMapper(authorizationRowMapper);

        return oAuth2AuthorizationService;
    }

    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository
    ) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    @Bean
    @Transactional
    public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException, InvalidKeySpecException {
        RSAKey rsaKey = rsaKey();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private RSAKey rsaKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Optional<KeyPairEntity> keyPairEntityOptional = keyPairRepository.findByKid(kid);

        RSAKey rsaKey;

        if (keyPairEntityOptional.isPresent()) {
            KeyPairEntity keyPairEntity = keyPairEntityOptional.get();

            RSAPublicKey publicKey = (RSAPublicKey) decodePublicKey(keyPairEntity.getPublicKey(), "RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) decodePrivateKey(keyPairEntity.getPrivateKey(), "RSA");

            rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(keyPairEntity.getKid())
                    .build();
        } else {
            KeyPair keyPair = generateRsaKey();

            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();

            KeyPairEntity keyPairEntity = new KeyPairEntity(
                    rsaKey.getKeyID(),
                    encodePublicKey(publicKey),
                    encodePrivateKey(privateKey)
            );

            keyPairRepository.save(keyPairEntity);
        }

        return rsaKey;
    }

    private KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    @Bean
    @Primary
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    public JwtDecoder googleDecoder(GoogleProvider googleProvider) {
        return googleProvider.jwtDecoder();
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer(UserRepository userRepository) {
        return context -> {
            Authentication authentication = context.getPrincipal();

            SecurityUser oldSecurityUser = (SecurityUser) authentication.getPrincipal();

            // keeping up to date with user info
            User user = userRepository.findById(oldSecurityUser.getUser().getId())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            SecurityUser securityUser = new SecurityUser(user);

            JwtClaimsSet.Builder claims = context.getClaims();

            claims.claim("uid", securityUser.getUser().getId());

            claims.claim("email_verified", securityUser.getUser().isEmailVerified());

            claims.claim("phone_verified", securityUser.getUser().isPhoneVerified());

            claims.claim("authorities",
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        };
    }

    @Bean
    public RequestMatcher providerRequestMatcher() {
        return new RegexRequestMatcher(Provider.PATTERN, HttpMethod.POST.name());
    }
}
