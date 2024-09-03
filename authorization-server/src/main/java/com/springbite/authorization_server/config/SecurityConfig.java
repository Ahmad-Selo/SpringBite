package com.springbite.authorization_server.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.springbite.authorization_server.entities.KeyPairEntity;
import com.springbite.authorization_server.entities.MailSenderEntity;
import com.springbite.authorization_server.handlers.CustomAuthenticationFailureHandler;
import com.springbite.authorization_server.handlers.CustomAuthenticationSuccessHandler;
import com.springbite.authorization_server.models.SecurityUser;
import com.springbite.authorization_server.repositories.KeyPairRepository;
import com.springbite.authorization_server.repositories.MailSenderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authorization.method.PrePostTemplateDefaults;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.springbite.authorization_server.utils.KeyPairUtil.*;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwk.kid}")
    private String kid;

    private final CustomAuthenticationSuccessHandler authenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler authenticationFailureHandler;
    private final MailSenderRepository mailSenderRepository;
    private final KeyPairRepository keyPairRepository;

    public SecurityConfig(
            CustomAuthenticationSuccessHandler authenticationSuccessHandler,
            CustomAuthenticationFailureHandler authenticationFailureHandler,
            MailSenderRepository mailSenderRepository,
            KeyPairRepository keyPairRepository
    ) {
        this.authenticationSuccessHandler = authenticationSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.mailSenderRepository = mailSenderRepository;
        this.keyPairRepository = keyPairRepository;
    }

    @Bean
    @ConfigurationProperties(prefix = "csrf")
    public CsrfWhiteSet csrfWhiteSet() {
        return new CsrfWhiteSet();
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(
                c -> c.successHandler(authenticationSuccessHandler)
                        .failureHandler(authenticationFailureHandler)
        );

        http.authorizeHttpRequests(
                c -> c.requestMatchers("/login", "/csrf", "/signup/**", "/auth/**",
                                "/forgot-password", "/verify-code", "/rest-password").permitAll()
                        .anyRequest().authenticated()
        );

        http.csrf(
                c -> c.csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                        .ignoringRequestMatchers(csrfWhiteSet().whiteSetUris.toArray(new String[0]))
        );

        return http.build();
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
    @Transactional
    public RSAKey rsaKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Optional<KeyPairEntity> keyPairEntityOptional = keyPairRepository.findByKid(kid);

        RSAKey rsaKey;

        if (keyPairEntityOptional.isPresent()) {
            KeyPairEntity keyPairEntity = keyPairEntityOptional.get();

            RSAPublicKey publicKey = decodePublicKey(keyPairEntity.getPublicKey());
            RSAPrivateKey privateKey = decodePrivateKey(keyPairEntity.getPrivateKey());

            rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(keyPairEntity.getKid())
                    .build();
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

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

            SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

            JwtClaimsSet.Builder claims = context.getClaims();

            claims.claim("uid", securityUser.getUser().getId());

            claims.claim("email_verified", securityUser.getUser().isEmailVerified());

            claims.claim("authorities",
                    authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()));
        };
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        long id = 1;

        MailSenderEntity mailSenderEntity = mailSenderRepository.findById(id).get();

        mailSender.setUsername(mailSenderEntity.getUsername());
        mailSender.setPassword(mailSenderEntity.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
